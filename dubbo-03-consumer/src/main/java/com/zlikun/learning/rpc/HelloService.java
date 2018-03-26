package com.zlikun.learning.rpc;

/**
 *
 * @author zlikun <zlikun-dev@hotmail.com>
 * @date 2018-03-21 20:46
 */
public interface HelloService {

    /**
     * 测试超时机制，传入一个毫秒数，服务端将休眠该毫秒数时间
     * @param mills
     * @param message
     * @return
     */
    String timeout(long mills, String message);

}
