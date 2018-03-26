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
        config.setName("dubbo-03-provider");
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
        config.setTimeout(2000);
        config.setRetries(0);
        config.setCluster("failover");
        // http://dubbo.io/books/dubbo-user-book/demos/config-connections.html
        // 限制服务端连接不超过5个，该参数只能在服务端配置，下面将测试connections参数对其影响
        // 当connections值比accepts值大的时候，会抛出如下异常
        // 2018-03-26 11:41:46.420 ERROR 10172 --- [erverWorker-5-5] c.a.d.remoting.transport.AbstractServer  :  [DUBBO] Close channel NettyChannel [channel=[id: 0x24ce7d61, L:/192.168.70.57:20880 - R:/192.168.70.57:57556]],
        // cause: The server /192.168.70.57:20880 connections greater than max config 5, dubbo version: 2.6.1, current host: 192.168.70.57
        config.setAccepts(2);
        // 客户端连接控制，限制客户端服务使用连接不超过10个
        // dubbo协议本身使用长连接，connections参数表示建立长连接数
        config.setConnections(3);
        // http://dubbo.io/books/dubbo-user-book/demos/concurrency-control.html
        // 并发控制，服务端并发执行或占用线程池线程数不超过5个，客户端超过这个线程数时，只接收指定线程数的请求，其它请求将失败
        // 实测：服务端executes参数可以约束actives参数(如：客户端)，限制其取值应小于等于executes的值，否则触发下面错误：
        // cause: The service using threads greater than <dubbo:service executes="5" /> limited.
        config.setExecutes(5);
        // 并发控制，客户端并发执行不超过10个，应指：客户端线程池容量上限，该配置可以被客户端覆盖，所以可以使用优先级更高的executes参数为控制
//        config.setActives(20);
        return config;
    }

}
