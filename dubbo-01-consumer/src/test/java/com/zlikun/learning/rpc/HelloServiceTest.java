package com.zlikun.learning.rpc;

import com.alibaba.dubbo.config.annotation.Reference;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

/**
 * @author zlikun <zlikun-dev@hotmail.com>
 * @date 2018-03-20 11:00
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class HelloServiceTest {

    // 提供者使用未使用注册服务，消费者通过url参数指定主机、端口来调用服务
    // @Reference(url = "dubbo://127.0.0.1:20880", group = "dev", version = "1.0.0")
    // 配置使用ZooKeeper作为注册中心
    @Reference(group = "dev", version = "1.0.0")
    private HelloService helloService;

    @Test
    public void say() {
       String message = helloService.say("zlikun");
       assertEquals("zlikun say 'Hello' !", message);
    }

}
