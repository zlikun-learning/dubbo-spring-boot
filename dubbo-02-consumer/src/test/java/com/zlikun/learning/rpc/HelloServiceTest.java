package com.zlikun.learning.rpc;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.dubbo.rpc.service.EchoService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;

/**
 * @author zlikun <zlikun-dev@hotmail.com>
 * @date 2018-03-20 11:00
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class HelloServiceTest {

    @Reference
    private HelloService helloService;

    @Test
    public void say() throws InterruptedException {
        log.info(helloService.say("Helen"));
        log.info(helloService.say("Ashe"));
        log.info(helloService.say("Jane"));

        // 每隔一秒调用一次，观察消费者执行情况
        for (int i = 0; i < 15; i++) {
            TimeUnit.SECONDS.sleep(1L);
            log.info(helloService.say(String.format("user_%02d", i)));
        }
    }

    /**
     * 回声测试、上下文测试
     * http://dubbo.io/books/dubbo-user-book/demos/echo-service.html
     * http://dubbo.io/books/dubbo-user-book/demos/context.html
     */
    @Test
    public void echo() {

        // 回声测试用于检测服务是否可用，回声测试按照正常请求流程执行，能够测试整个调用是否通畅，可用于监控。
        EchoService echoService = (EchoService) helloService;
        Object status = echoService.$echo("ok");
        log.info("echo -> {}", status);                             // echo -> ok

        // RpcContext 是一个 ThreadLocal 的临时状态记录器，当接收到 RPC 请求，或发起 RPC 请求时，RpcContext 的状态都会变化

        // 获取上下文实例
        RpcContext context = RpcContext.getContext();

        // 本端是否消费端
        assertTrue(context.isConsumerSide());
        // 最后一次调用提供方IP
        log.info("remote_host = {}", context.getRemoteHost());      // remote_host = 192.168.70.57
        log.info("local_host = {}", context.getLocalHost());        // local_host = 192.168.70.57
        // 获取当前服务配置信息(所有配置信息都被转换为URL参数)
        log.info("url = {}", context.getUrl().getAbsolutePath());    // url = /com.zlikun.learning.rpc.HelloService
        // 获取到的配置参数输出信息，见页底
        context.getUrl().getParameters().forEach((key, value) -> {
            log.info("param_key = {}, param_value = {}", key, value);
        });

        // 再次发起接口调用，每次发起RPC调用，上下文状态会随之变化
        // otherService.otherMethod();

    }

    /**
     * 隐式参数
     * http://dubbo.io/books/dubbo-user-book/demos/attachment.html
     */
    @Test
    public void attachment() {
        // 调用前设置一个隐式参数
        RpcContext.getContext().setAttachment("author", "zlikun");
        // 执行调用，隐式参数将传递给服务端
        helloService.say("zlikun");
    }

    /**
     * 异步调用，目前注解方式还无法配置方法级的异步，所以采用配置两个消费端方式实现区分
     * http://dubbo.io/books/dubbo-user-book/demos/async-call.html
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void async() throws ExecutionException, InterruptedException {
        // 执行方法调用，调用后方法立即返回
        helloService.async("dubbo");
        // 方法执行完成后会通知和设置Future，以获取结果
        Future<String> futureDubbo = RpcContext.getContext().getFuture();

        // 再次调用方法
        helloService.async("spring");
        Future<String> futureSpring = RpcContext.getContext().getFuture();

        // 此时两个方法都在执行，执行耗时取决于两者耗时久的那一个
        String dubboMessage = futureDubbo.get();
        String springMessage = futureSpring.get();

        // dubbo = async_dubbo, spring = async_spring
        log.info("dubbo = {}, spring = {}", dubboMessage, springMessage);
    }

}
/* ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
param_key = owner, param_value = zlikun
param_key = default.cluster, param_value = failover
param_key = default.group, param_value = dev
param_key = default.version, param_value = 1.0.0
param_key = side, param_value = consumer
param_key = register.ip, param_value = 192.168.70.57
param_key = methods, param_value = say
param_key = logger, param_value = slf4j
param_key = default.check, param_value = false
param_key = dubbo, param_value = 2.6.1
param_key = pid, param_value = 12000
param_key = monitor, param_value = dubbo%3A%2F%2F192.168.120.250%3A2181%2Fcom.alibaba.dubbo.registry.RegistryService%3Fapplication%3Ddubbo-02-consumer%26client%3Dcurator%26dubbo%3D2.6.1%26logger%3Dslf4j%26organization%3Dzlikun.com%26owner%3Dzlikun%26pid%3D12000%26protocol%3Dregistry%26refer%3Ddubbo%253D2.6.1%2526interface%253Dcom.alibaba.dubbo.monitor.MonitorService%2526pid%253D12000%2526timestamp%253D1521782485080%26register%3Dtrue%26registry%3Dzookeeper%26timestamp%3D1521782485078
param_key = check, param_value = false
param_key = default.server, param_value = netty4
param_key = interface, param_value =
param_key = generic, param_value = false
param_key = application, param_value =
param_key = default.client, param_value = netty4
param_key = default.timeout, param_value = 3000
param_key = organization, param_value = zlikun.com
param_key = remote.timestamp, param_value =
param_key = anyhost, param_value = true
param_key = timestamp, param_value = 1521782485057
------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ */