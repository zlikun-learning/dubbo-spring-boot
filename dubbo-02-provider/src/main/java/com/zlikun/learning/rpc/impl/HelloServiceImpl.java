package com.zlikun.learning.rpc.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.RpcContext;
import com.zlikun.learning.rpc.HelloService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * 通过注解发布服务
 * @author zlikun <zlikun-dev@hotmail.com>
 * @date 2018-03-21 20:47
 */
@Slf4j
@Component
@Service(timeout = 500, retries = 0)
public class HelloServiceImpl implements HelloService {

    @Override
    public String say(String name) {
        // http://dubbo.io/books/dubbo-user-book/demos/attachment.html
        // 获取消费端传递的隐式参数
        String author = RpcContext.getContext().getAttachment("author");
        // 服务提供者接收到的隐式参数：zlikun
        log.info("服务提供者接收到的隐式参数：{}", author);
        return String.format("%s : Hello Guys !", name);
    }

    @Override
    public String async(String message) {
        log.info("execute async method, message = {}", message);
        return "async_" + message;
    }

    @Override
    public String timeout(long mills, String message) {
        long begin = System.currentTimeMillis();
        try {
            Thread.sleep(mills);
            return "timeout_method_" + message;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            long end = System.currentTimeMillis();
            log.info("----------------> begin = {}, end = {}, elapsed = {} 毫秒!",
                    LocalDateTime.ofInstant(Instant.ofEpochMilli(begin), ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss.SSS")),
                    LocalDateTime.ofInstant(Instant.ofEpochMilli(end), ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss.SSS")),
                    end - begin);
        }
        return null;
    }
}
