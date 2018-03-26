package com.zlikun.learning.rpc;

import com.alibaba.dubbo.config.annotation.Reference;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 集群容错测试：failover、failfast、failsafe、failback、forking、broadcast
 * http://dubbo.io/books/dubbo-user-book/demos/fault-tolerent-strategy.html
 * @author zlikun <zlikun-dev@hotmail.com>
 * @date 2018-03-260 09:50
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class ClusterToleranceTest {

    @Reference(timeout = 100, cluster = "failover")
    private HelloService helloService;

    @Test
    public void test() {

        String message = helloService.timeout(300, "zlikun");
        log.info("rpc response message => {}", message);

    }

}