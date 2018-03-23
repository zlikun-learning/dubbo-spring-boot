package com.zlikun.learning;

import com.alibaba.dubbo.config.spring.context.annotation.DubboComponentScan;
import com.zlikun.learning.service.LogicService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author zlikun <zlikun-dev@hotmail.com>
 * @date 2018-03-20 10:44
 */
@Slf4j
@SpringBootApplication
@DubboComponentScan("com.zlikun.learning.service")
public class DubboConsumerLauncher {

    public static void main(String[] args) throws InterruptedException {
        ConfigurableApplicationContext context = SpringApplication.run(DubboConsumerLauncher.class, args);
        context.start();

        // 测试服务消费
        LogicService logicService = context.getBean(LogicService.class);
        // 回声测试
        Object message = logicService.echo();
        // echo -> ok
        log.info("echo -> {}", message);
        // 业务测试
        logicService.consume();

        context.stop();
        context.close();
    }

}
