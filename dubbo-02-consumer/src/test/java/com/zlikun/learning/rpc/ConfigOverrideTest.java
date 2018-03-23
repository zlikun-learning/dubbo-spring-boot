package com.zlikun.learning.rpc;

import com.alibaba.dubbo.config.annotation.Reference;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

/**
 * Dubbo配置覆盖测试，参考下述文档《配置覆盖关系》一节所述内容
 * http://dubbo.io/books/dubbo-user-book/configuration/xml.html
 * @author zlikun <zlikun-dev@hotmail.com>
 * @date 2018-03-23 13:57
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class ConfigOverrideTest {

//    @Reference(timeout = 200, retries = 0)
    @Reference(retries = 0)
    private HelloService helloService;

    /**
     * 测试超时配置覆盖(基于注解配置服务、消费者时，暂无法配置到方法级别，所以从接口级别开始测试)
     * reference_method > service_method > reference > service > consumer > provider
     * --               > --             > 200       > 500     > 1000     > 2000
     *
     * Failed to invoke the method timeout in the service com.zlikun.learning.rpc.HelloService.
     * Last error is: Invoke remote method timeout. method: timeout, provider: ******
     * cause: Waiting server-side response timeout. start time: 2018-03-23 14:07:22.356, end time: 2018-03-23 14:07:22.557, client elapsed: 0 ms, server elapsed: 201 ms, timeout: 200 ms
     */
    @Test
    public void timeout() {
        log.info("begin ...");
        try {
//            // 2018-03-23 14:23:46.708 -> 2018-03-23 14:23:46.884，客户端执行耗时：884 - 708 = 176 毫秒，服务端实际执行耗时比该值小，双方都未超时，Dubbo调用成功
//            assertEquals("timeout_method_reference", helloService.timeout(100, "reference"));

//            // 当发生调用超时，服务端仍完成请求，但响应时会通知超时
//            // 2018-03-23 14:16:00.232 -> 2018-03-23 14:16:00.451，客户端执行耗时：451 - 232 = 229 毫秒，即：reference配置为200毫秒超时生效，覆盖了service及更上层的值
//            assertEquals("timeout_method_service", helloService.timeout(300, "service"));

            // 该组测试时，将 reference 中的超时配置移除，此时 service 中的超时配置将生效
            // 服务端：2018-03-23 14:26:11.311 -> 2018-03-23 14:26:12.061 = 750 毫秒
            // 客户端：2018-03-23 14:26:11.248 -> 2018-03-23 14:26:11.763 = 515 毫秒
            // 客户端超过 service 中的 500 设定，实际发生了超时，服务端仍完成了请求，但在完成请求之前，Dubbo以超时异常返回给了客户端
            assertEquals("timeout_method_consumer", helloService.timeout(750, "consumer"));

//        assertEquals("timeout_method_provider", helloService.timeout(1500, "provider"));
        } finally {
            log.info("end ...");
        }
    }

}
