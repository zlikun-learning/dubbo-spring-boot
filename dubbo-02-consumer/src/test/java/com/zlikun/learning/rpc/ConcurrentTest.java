package com.zlikun.learning.rpc;

import com.alibaba.dubbo.config.annotation.Reference;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author zlikun <zlikun-dev@hotmail.com>
 * @date 2018-03-23 16:16
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class ConcurrentTest {

    @Reference(timeout = 300, actives = 15)
    private HelloService helloService;

    /**
     * 并发控制
     * http://dubbo.io/books/dubbo-user-book/demos/concurrency-control.html
     *
     * reference_actives = 5, service_actives = 10
     * 实际测试中，8个线程并发执行接口调用请求时，触发了并发限制(异常)
     * Waiting concurrent invoke timeout in client-side for service:  com.zlikun.learning.rpc.HelloService, method: timeout,
     * elapsed: 300, timeout: 300. concurrent invokes: 5. max concurrent invoke limit: 5
     * reference_actives = 15, service_actives = 10
     * 调整大于实际并发数后，不再抛出异常
     * @throws InterruptedException
     */
    @Test
    public void timeout() throws InterruptedException {
        // 启动8个线程
        for (int i = 0; i < 8; i++) {
            // 每个线程执行20次接口调用
            int index = i;
            new Thread(() -> {
                for (int j = 0; j < 20; j++) {
                    helloService.timeout(150, String.format("user_%d_%02d", index, j));
                }
            }).start();
        }
        Thread.currentThread().join(30_000L);
    }

}
