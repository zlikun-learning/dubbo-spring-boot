package com.zlikun.learning.conf;

import com.alibaba.dubbo.config.*;
import com.zlikun.learning.rpc.HelloService;
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
        config.setName("dubbo-01-provider");    // 服务治理：应用名称，必填
        config.setOrganization("zlikun.com");
        config.setOwner("zlikun");
        config.setLogger("slf4j");                // 性能优化：日志输出方式，可选值 [slf4j, jcl, log4j, jdk]
        config.setEnvironment("develop");         // 服务治理：应用环境，可选值 [ develop, test, product ]
        config.setVersion("v1.0");                 // 服务治理：当前应用的版本 ( 实际意义是什么？ )
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
        config.setId("master");             // 配置关联：多注册中心时配置，用于区分配置中心，可以在service或reference节点中使用该值
        config.setProtocol("zookeeper");    // 服务发现：支持dubbo、http、local，实际常用于与address配合配置zookeeper集群
        // http://dubbo.io/books/dubbo-user-book/references/registry/zookeeper.html
        // 单台格式：zookeeper://10.20.153.10:2181
        // 集群格式：zookeeper://10.20.153.10:2181?backup=10.20.153.11:2181,10.20.153.12:2181
        // protocol="zookeeper" address="10.20.153.10:2181,10.20.153.11:2181,10.20.153.12:2181"
        config.setAddress(address);          // 服务发现：注册中心地址，必填，推荐使用zookeeper
        config.setRegister(true);            // 服务治理：是否向注册中心注册服务，默认：true，可选
        config.setTimeout(5000);              // 性能调优：注册中心请求超时毫秒数，默认：5000，可选
        config.setSession(60000);             // 性能调优：注册中心会话超时毫秒数，默认：60000，可选，常用于检测提供者非正常断线后的脏数据，如：心跳检测就是用此时间为间隔时间
        config.setCheck(true);               // 服务治理：注册中心不存在时是否报错，默认：true，可选
        config.setClient("curator");        // 连接客户端：这里手动配置为curator库
        config.setTransporter("netty4");     // 性能调优：网络传输方式，可选 [mina, netty]，2.5.8以后增加netty4
        return config;
    }

    /**
     * 备用注册中心，注册中心可以配置多个
     * @return
     */
    @Bean
    public RegistryConfig registryConfigSlave() {
        RegistryConfig config = new RegistryConfig();
        config.setId("slave");
        config.setProtocol("zookeeper");
        config.setAddress(address);
        config.setRegister(true);
        config.setTimeout(5000);
        config.setSession(60000);
        config.setCheck(false);
        config.setClient("curator");
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
        config.setName("dubbo");                // 性能调优：协议名称，必填，默认：dubbo
        config.setPort(-1);                      // 服务发现：服务端口，可选，默认：dubbo协议20880、rmi协议1099、hessian协议80，如果配置为-1或者不配置，则会自动分配一个未被使用的端口
        config.setSerialization("hessian2");    // 性能调优：协议序列化方式，可选，默认：dubbo协议hessian2、rmi协议java、http协议json
        config.setThreads(100);                  // 性能调优：服务线程池大小(固定大小)，可选，默认：100
        config.setAccepts(10);                   // 性能调优：服务方最大可连接数，可选，默认：0(不限)
        config.setPayload(1024 * 1024);          // 性能调优：请求及响应数据包大小限制，单位：字节，默认：88388608(=8M)
        config.setTransporter("netty4");        // 性能调优：网络传输方式，可选 [mina, netty]，2.5.8以后增加netty4
        config.setRegister(true);                // 服务治理：该协议的服务是否注册到注册中心，可选，默认：true
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
        config.setProtocol("registry"); // 服务治理：监控中心协议，可选，默认：dubbo，如果值为：registry，表示从注册中心发现监控中心地址，否则直连监控中心
        // config.setAddress("");         // 服务治理：直连监控中心的服务器地址，可选
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
        config.setServer("netty4");     // 性能调优：协议的服务器端实现类型，比如：dubbo协议的mina,netty等，http协议的jetty,servlet等，可选
        config.setGroup(group);
        config.setVersion(version);
        config.setTimeout(3000);
        config.setRetries(2);
        config.setQueues(0);             // 性能调优：线程池队列大小，当线程池满时，排队等待执行的队列大小，建议不要设置，当线程程池时应立即失败，重试其它服务提供机器，而不是排队，除非有特殊需求，可选，默认值：0
        config.setConnections(1);        // 性能调优：对每个提供者的最大连接数，rmi、http、hessian等短连接协议表示限制连接数，dubbo等长连接协表示建立的长连接个数，可选，默认值：0
        config.setThreads(10);           // 性能调优：服务线程池大小(固定大小)，可选，默认值：100
        config.setAccepts(10);
        config.setPayload(1024 * 1024);
        config.setExecutes(3);           // 性能调优：服务提供者每服务每方法最大可并行执行请求数，可选，默认值;0
        config.setActives(3);            // 性能调优：每服务消费者每服务每方法最大并发调用数，可选，默认值;0
        config.setCluster("failover");  // 性能调优：集群方式，可选：failover/failfast/failsafe/failback/forking，可选，默认值：failover
        config.setExport(true);
        config.setToken(true);
        config.setLayer("rpc");
        config.setAccesslog(true);
        config.setDocument("https://zlikun.com/dubbo/provider");
        return config;
    }

    // ================================================== 发布服务==================================================

    /**
     * 发布服务，注意：ServiceConfig为重对象，内部封装了与注册中心的连接，以及开启服务端口
     * http://dubbo.io/books/dubbo-user-book/configuration/api.html
     * http://dubbo.io/books/dubbo-user-book/references/xml/dubbo-service.html
     * @param helloService
     * @return
     */
    @Bean
    public ServiceConfig<HelloService> helloServiceDubbo(HelloService helloService) {
        ServiceConfig<HelloService> config = new ServiceConfig<>();
        config.setApplication(applicationConfig());
        config.setProtocol(protocolConfig());
        config.setRegistry(registryConfig());
        config.setProvider(providerConfig());
        config.setAccesslog(true);
        config.setCluster("failover");

        config.setInterface(HelloService.class);    // 服务发现：服务接口名，必填
        config.setRef(helloService);                 // 服务发现：服务对象引用实现，必填

        // 暴露及注册服务
        config.export();

        return config;
    }

}
