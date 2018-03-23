package com.zlikun.learning.rpc.impl;

import com.zlikun.learning.rpc.HelloService;
import org.springframework.stereotype.Component;

/**
 * @author zlikun <zlikun-dev@hotmail.com>
 * @date 2018-03-21 20:47
 */
@Component
public class HelloServiceImpl implements HelloService {

    @Override
    public String say(String name) {
        return String.format("%s : Hello Guys !", name);
    }

}
