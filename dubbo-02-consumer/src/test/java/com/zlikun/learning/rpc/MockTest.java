package com.zlikun.learning.rpc;

import com.alibaba.dubbo.config.annotation.Reference;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author zlikun <zlikun-dev@hotmail.com>
 * @date 2018-03-23 15:57
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class MockTest {

    @Reference
    private HelloService helloService;

    @Test
    public void say() {
        String message = helloService.say("zlikun");
        log.info("message = {}", message);
    }

}
