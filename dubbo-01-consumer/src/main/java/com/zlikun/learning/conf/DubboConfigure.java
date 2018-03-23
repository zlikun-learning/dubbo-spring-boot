package com.zlikun.learning.conf;

import com.alibaba.dubbo.config.*;
import com.zlikun.learning.rpc.HelloService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

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

    /**
     * 配置应用
     * http://dubbo.io/books/dubbo-user-book/references/xml/dubbo-application.html
     * @return
     */
    @Bean
    public ApplicationConfig applicationConfig() {
        ApplicationConfig config = new ApplicationConfig();
        config.setName("dubbo-01-consumer");
        config.setOrganization("zlikun.com");
        config.setOwner("zlikun");
        config.setLogger("slf4j");
        config.setEnvironment("develop");
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
        config.setId("master");
        config.setProtocol("zookeeper");
        config.setAddress(address);
        config.setRegister(true);
        config.setTimeout(5000);
        config.setSession(60000);
        config.setCheck(true);
        config.setClient("curator");
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
        config.setGroup(group);
        return config;
    }

    /**
     * 配置服务消费者(通用)
     * http://dubbo.io/books/dubbo-user-book/references/xml/dubbo-consumer.html
     * http://dubbo.io/books/dubbo-user-book/demos/netty4.html
     * @return
     */
    @Bean
    public ConsumerConfig consumerConfig() {
        ConsumerConfig config = new ConsumerConfig();
        config.setClient("netty4");
        config.setGroup(group);
        config.setVersion(version);
        config.setTimeout(3000);
        config.setRetries(0);
        config.setConnections(1);
        config.setCheck(false);
        config.setLoadbalance("roundrobin");
        config.setActives(3);
        config.setCluster("failover");
        config.setLayer("rpc");
        return config;
    }

    // ================================================== 消费服务==================================================

    /**
     * 引用服务，注意：ReferenceConfig为重对象，内部封装了与注册中心的连接，以及与服务提供方的连接
     * http://dubbo.io/books/dubbo-user-book/configuration/api.html
     * http://dubbo.io/books/dubbo-user-book/references/xml/dubbo-reference.html
     * @return
     */
    @Bean
    public HelloService helloServiceDubbo() {
        ReferenceConfig<HelloService> config = new ReferenceConfig<>();
        config.setApplication(applicationConfig());
        config.setProtocol("dubbo");
        config.setRegistry(registryConfig());
        config.setConsumer(consumerConfig());
        config.setCluster("failover");

        config.setInterface(HelloService.class);

        return config.get();
    }

}
