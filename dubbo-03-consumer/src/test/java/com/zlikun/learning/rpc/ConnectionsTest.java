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

/**
 * 连接控制测试
 * http://dubbo.io/books/dubbo-user-book/demos/config-connections.html
 * @author zlikun <zlikun-dev@hotmail.com>
 * @date 2018-03-260 09:50
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class ConnectionsTest {

    @Reference(timeout = 200, cluster = "failfast", connections = 5)
    private HelloService helloService;

    @Test
    public void test() {

        int threads = 20;

        // 构建20个线程，并发请求
        ExecutorService exec = Executors.newFixedThreadPool(20);
        CountDownLatch latch = new CountDownLatch(threads);

        for (int i = 0; i < threads; i++) {
            int index = i;
            exec.execute(() -> {
                latch.countDown();
                // 控制任务同时执行，以构成并发
                while (latch.getCount() != 0L) ;
                log.info("begin ...");
                String message = helloService.timeout(50, "zlikun_" + index);
                log.info("end => {}", message);
            });
        }

        exec.shutdown();
        while (!exec.isTerminated());

        log.info("The connections test was completed .");

    }

}