package com.zlikun.learning.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.rpc.service.EchoService;
import com.zlikun.learning.rpc.HelloService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author zlikun <zlikun-dev@hotmail.com>
 * @date 2018-03-23 11:29
 */
@Slf4j
@Service
public class LogicService {

    @Reference
    private HelloService helloService;

    /**
     * 回声测试
     * http://dubbo.io/books/dubbo-user-book/demos/echo-service.html
     */
    public Object echo() {
        EchoService echoService = (EchoService) helloService;
        return echoService.$echo("ok");
    }

    /**
     * 测试服务消费者
     */
    public void consume() throws InterruptedException {
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
