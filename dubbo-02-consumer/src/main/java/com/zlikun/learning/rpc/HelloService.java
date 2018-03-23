package com.zlikun.learning.rpc;

/**
 *
 * @author zlikun <zlikun-dev@hotmail.com>
 * @date 2018-03-21 20:46
 */
public interface HelloService {

    String say(String name);

    /**
     * 异步调用测试
     * @param message
     * @return
     */
    String async(String message);

    /**
     * 测试超时机制，传入一个毫秒数，服务端将休眠该毫秒数时间
     * @param mills
     * @param message
     * @return
     */
    String timeout(long mills, String message);

}
