//package com.zlikun.learning.rpc.mock;
//
//import com.alibaba.dubbo.config.annotation.Service;
//import com.zlikun.learning.rpc.HelloService;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//
///**
// * 本地伪装，但具体要怎么用？
// * http://dubbo.io/books/dubbo-user-book/demos/local-mock.html
// * @author zlikun <zlikun-dev@hotmail.com>
// * @date 2018-03-23 15:56
// */
//@Slf4j
//@Component
//@Service(group = "dev", version = "1.0.0", mock = "true")
//public class HelloServiceMock implements HelloService {
//
//    @Override
//    public String say(String name) {
//        log.info("execute mock method ...");
//        return "mock_" + name;
//    }
//
//    @Override
//    public String async(String message) {
//        return null;
//    }
//
//    @Override
//    public String timeout(long mills, String message) {
//        return null;
//    }
//}
