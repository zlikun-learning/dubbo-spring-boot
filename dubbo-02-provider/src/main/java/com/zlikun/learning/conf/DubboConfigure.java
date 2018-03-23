package com.zlikun.learning.conf;

import com.alibaba.dubbo.config.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

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

    /**
     * 配置应用
     * http://dubbo.io/books/dubbo-user-book/references/xml/dubbo-application.html
     * @return
     */
    @Bean
    public ApplicationConfig applicationConfig() {
        ApplicationConfig config = new ApplicationConfig();
        config.setName("dubbo-02-provider");
        config.setOrganization("zlikun.com");
        config.setOwner("zlikun");
        config.setLogger("slf4j");
        return config;
    }

    /**
     * 配置注册中心
     * http://dubbo.io/books/dubbo-user-book/references/xml/dubbo-registry.html
     * @return
     */
    @Bean
    @Primary
    public RegistryConfig registryConfig() {
        RegistryConfig config = new RegistryConfig();
        config.setProtocol("zookeeper");
        config.setAddress(address);
        config.setClient("curator");
        config.setTransporter("netty4");
        return config;
    }

    /**
     * 配置协议
     * http://dubbo.io/books/dubbo-user-book/references/xml/dubbo-protocol.html
     * @return
     */
    @Bean
    public ProtocolConfig protocolConfig() {
        ProtocolConfig config = new ProtocolConfig();
        config.setName("dubbo");
        config.setPort(-1);
        config.setTransporter("netty4");
        return config;
    }

    /**
     * 配置监视器
     * http://dubbo.io/books/dubbo-user-book/references/xml/dubbo-monitor.html
     * @return
     */
    @Bean
    public MonitorConfig monitorConfig() {
        MonitorConfig config = new MonitorConfig();
        config.setProtocol("registry");
        return config;
    }

    /**
     * 配置服务提供者(通用)
     * http://dubbo.io/books/dubbo-user-book/references/xml/dubbo-provider.html
     * http://dubbo.io/books/dubbo-user-book/demos/netty4.html
     * @return
     */
    @Bean
    public ProviderConfig providerConfig() {
        ProviderConfig config = new ProviderConfig();
        config.setServer("netty4");
        config.setGroup(group);
        config.setVersion(version);
        config.setTimeout(3000);
        config.setCluster("failover");
        return config;
    }

}
