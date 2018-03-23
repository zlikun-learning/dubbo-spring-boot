package com.zlikun.learning;

import com.zlikun.learning.rpc.HelloService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.concurrent.TimeUnit;

/**
 * @author zlikun <zlikun-dev@hotmail.com>
 * @date 2018-03-20 10:44
 */
@Slf4j
@SpringBootApplication
public class DubboConsumerLauncher {

    public static void main(String[] args) throws InterruptedException {
        ConfigurableApplicationContext context = SpringApplication.run(DubboConsumerLauncher.class, args);
        context.start();

        // 测试服务消费
        consume(context);

        context.stop();
        context.close();
    }

    /**
     * 测试服务消费者
     * @param context
     */
    private static final void consume(ConfigurableApplicationContext context) throws InterruptedException {
        HelloService helloService = context.getBean(HelloService.class);
        log.info(helloService.say("Helen"));
        log.info(helloService.say("Ashe"));
        log.info(helloService.say("Jane"));

        // 每隔一秒调用一次，观察消费者执行情况
        for (int i = 0; i < 60; i++) {
            TimeUnit.SECONDS.sleep(1L);
            log.info(helloService.say(String.format("user_%02d", i)));
        }
    }

}
