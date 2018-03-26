package com.zlikun.learning;

import com.alibaba.dubbo.config.spring.context.annotation.DubboComponentScan;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author zlikun <zlikun-dev@hotmail.com>
 * @date 2018-03-20 10:44
 */
@Slf4j
@SpringBootApplication
@DubboComponentScan
public class DubboConsumerLauncher {

    public static void main(String[] args) {
        SpringApplication.run(DubboConsumerLauncher.class, args);
    }

}
