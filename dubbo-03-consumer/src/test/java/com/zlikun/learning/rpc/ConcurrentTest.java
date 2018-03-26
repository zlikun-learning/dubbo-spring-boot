package com.zlikun.learning.rpc;

import com.alibaba.dubbo.config.annotation.Reference;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 并发控制测试
 * http://dubbo.io/books/dubbo-user-book/demos/concurrency-control.html
 * @author zlikun <zlikun-dev@hotmail.com>
 * @date 2018-03-260 09:50
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class ConcurrentTest {

    /**
     * 当客户端actives取值小于服务端的executes时(如：服务端为5、客户端为2)，服务才完全可用，
     * 但如果客户端时间并发远大于actives值(actives值为2，但实际并发为10)，将造成等待超时(客户端)
     *
     * 同上述关系一致，connections参数受服务端accepts参数控制，不能大于accepts的值
     * connections主要用于控制客户端的连接数(长连接等)，该参数并不等同于并发数
     */
    @Reference(timeout = 500, cluster = "failfast", actives = 5, connections = 1)
    private HelloService helloService;

    private final AtomicLong counter = new AtomicLong();

    @Test
    public void test() {

        int threads = 10;

        // 构建20个线程，并发请求
        ExecutorService exec = Executors.newFixedThreadPool(threads);
        CountDownLatch latch = new CountDownLatch(threads);

        for (int i = 0; i < threads; i++) {
            int index = i;
            exec.execute(() -> {
                latch.countDown();
                // 控制任务同时执行，以构成并发
                while (latch.getCount() != 0L) ;
                // 每个线程跑5次任务
                for (int j = 0; j < 10; j++) {
                    long count = counter.incrementAndGet();
                    log.info("[{}] begin => {} / {}", count, index, j);
                    String message = helloService.timeout(50, "zlikun_" + index + "_" + j);
                    log.info("[{}] end => {} / {}, {}", count, index, j, message);
                }
            });
        }

        exec.shutdown();
        while (!exec.isTerminated());

        log.info("The connections test was completed .");

    }

}