package com.zlikun.learning;

import com.alibaba.dubbo.config.spring.context.annotation.DubboComponentScan;
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
@DubboComponentScan("com.zlikun.learning.rpc")
public class DubboProviderLauncher {

    public static void main(String[] args) throws InterruptedException {
        ConfigurableApplicationContext context = SpringApplication.run(DubboProviderLauncher.class, args);
        context.start();
        log.info("Dubbo provider is running ...");
        // 主线程阻塞30分钟，测试Dubbo服务提供者
        Thread.currentThread().join(30 * 60 * 1000L);
        context.stop();
        context.close();
    }

}
