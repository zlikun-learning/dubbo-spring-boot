package com.zlikun.learning.conf;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ConsumerConfig;
import com.alibaba.dubbo.config.MonitorConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 通过API配置Dubbo服务消费者
 * http://dubbo.io/books/dubbo-user-book/configuration/api.html
 * @author zlikun <zlikun-dev@hotmail.com>
 * @date 2018-03-23 09:03
 */
@Configuration
public class DubboConfigure {

    private String address = "192.168.120.250:2181";
    private String group = "dev";
    private String version = "1.0.0";

    @Bean
    public ApplicationConfig applicationConfig() {
        ApplicationConfig config = new ApplicationConfig();
        config.setName("dubbo-04-consumer");
        config.setOrganization("zlikun.com");
        config.setOwner("zlikun");
        config.setLogger("slf4j");
        return config;
    }

    @Bean
    public RegistryConfig registryConfig() {
        RegistryConfig config = new RegistryConfig();
        config.setProtocol("zookeeper");
        config.setAddress(address);
        config.setRegister(true);
        config.setClient("curator");
        return config;
    }

    @Bean
    public MonitorConfig monitorConfig() {
        MonitorConfig config = new MonitorConfig();
        config.setProtocol("registry");
        return config;
    }

    @Bean
    public ConsumerConfig consumerConfig() {
        ConsumerConfig config = new ConsumerConfig();
        config.setClient("netty4");
        config.setGroup(group);
        config.setVersion(version);
        config.setTimeout(1000);
        config.setCheck(false);
        return config;
    }

}
