package com.zlikun.learning.conf;

import com.alibaba.dubbo.config.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 通过API配置Dubbo服务提供者
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
        config.setName("dubbo-04-provider");
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
        config.setClient("curator");
        config.setTransporter("netty4");
        return config;
    }

    @Bean
    public ProtocolConfig protocolConfig() {
        ProtocolConfig config = new ProtocolConfig();
        config.setName("dubbo");
        config.setTransporter("netty4");
        return config;
    }

    @Bean
    public MonitorConfig monitorConfig() {
        MonitorConfig config = new MonitorConfig();
        config.setProtocol("registry");
        return config;
    }

    @Bean
    public ProviderConfig providerConfig() {
        ProviderConfig config = new ProviderConfig();
        config.setServer("netty4");
        config.setGroup(group);
        config.setVersion(version);
        config.setTimeout(2000);
        config.setRetries(0);
        config.setCluster("failover");

        config.setApplication(applicationConfig());
        config.setRegistry(registryConfig());
        config.setProtocol(protocolConfig());
        config.setMonitor(monitorConfig());

        return config;
    }

}
