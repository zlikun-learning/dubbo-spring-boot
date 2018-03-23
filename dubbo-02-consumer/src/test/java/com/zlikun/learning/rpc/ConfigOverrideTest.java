package com.zlikun.learning.rpc;

import com.alibaba.dubbo.config.annotation.Reference;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import static org.junit.Assert.assertEquals;

/**
 * Dubbo配置覆盖测试，参考下述文档《配置覆盖关系》一节所述内容
 * http://dubbo.io/books/dubbo-user-book/configuration/xml.html
 *
 * 配置覆盖关系
 * 以 timeout 为例，显示了配置的查找顺序，其它 retries, loadbalance, actives 等类似：
 * 方法级优先，接口级次之，全局配置再次之。
 * 如果级别一样，则消费方优先，提供方次之。
 * 其中，服务提供方配置，通过 URL 经由注册中心传递给消费方。
 *
 * @author zlikun <zlikun-dev@hotmail.com>
 * @date 2018-03-23 13:57
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class ConfigOverrideTest {

//    @Reference(timeout = 200, retries = 0)
//    @Reference(retries = 0)
    @Reference(timeout = 100, retries = 4)
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
            // 2018-03-23 14:23:46.708 -> 2018-03-23 14:23:46.884，客户端执行耗时：884 - 708 = 176 毫秒，服务端实际执行耗时比该值小，双方都未超时，Dubbo调用成功
            assertEquals("timeout_method_reference", helloService.timeout(100, "reference"));

//            // 当发生调用超时，服务端仍完成请求，但响应时会通知超时
//            // 2018-03-23 14:16:00.232 -> 2018-03-23 14:16:00.451，客户端执行耗时：451 - 232 = 229 毫秒，即：reference配置为200毫秒超时生效，覆盖了service及更上层的值
//            assertEquals("timeout_method_service", helloService.timeout(300, "service"));

//            // 该组测试时，将 reference 中的超时配置移除，此时 service 中的超时配置将生效
//            // 服务端：2018-03-23 14:26:11.311 -> 2018-03-23 14:26:12.061 = 750 毫秒
//            // 客户端：2018-03-23 14:26:11.248 -> 2018-03-23 14:26:11.763 = 515 毫秒
//            // 客户端超过 service 中的 500 设定，实际发生了超时，服务端仍完成了请求，但在完成请求之前，Dubbo以超时异常返回给了客户端
//            assertEquals("timeout_method_consumer", helloService.timeout(750, "consumer"));

//        assertEquals("timeout_method_provider", helloService.timeout(1500, "provider"));
        } finally {
            log.info("end ...");
        }
    }

    /**
     * 重试配置覆盖测试
     * reference_method > service_method > reference > service > consumer > provider
     * --               > --             > 0         > 1       > 2        > 3
     * --               > --             > 0         > 0       > 2        > 3
     * --               > --             > 4         > 0       > 2        > 3
     */
    @Test(expected = Exception.class)
    public void retries() {
        long begin = System.currentTimeMillis();
        try {
            // 目前 reference_timeout = 100，所以下面调用一定会超时，此时将触发重试
            helloService.timeout(200, "retries");
        } finally {
            long end = System.currentTimeMillis();

            // 第一组
            // 客户端：
            // begin = 2018/03/23 15:02:58.879, end = 2018/03/23 15:02:59.101, elapsed = 222 毫秒!
            // 服务端：
            // 输出两次日志，即：发生过一次重试，与service_reties取值吻合，即：reference_retries配置并未覆盖上层配置
            // 第一次：begin = 2018/03/23 15:02:59.002, end = 2018/03/23 15:02:59.202, elapsed = 200 毫秒，服务端实际完成了请求，但超过客户端设置的超时时间
            // 第二次：begin = 2018/03/23 15:02:58.998, end = 2018/03/23 15:02:59.198, elapsed = 200 毫秒，服务端实际完成了请求，但超过客户端设置的超时时间
            // 比较两个请求开始时间，第二次请求比第一次请求晚4ms，实际应比较与客户端调用开始时间差，差值为：119ms，所以客户端判断超时后，发生了重试，并且只重试了一次
            // 第二组，将 service_reties 设置为0
            // 客户端：
            // begin = 2018/03/23 15:16:16.529, end = 2018/03/23 15:16:16.848, elapsed = 319 毫秒!
            // 服务端：
            // begin = 2018/03/23 15:16:16.660, end = 2018/03/23 15:16:16.867, elapsed = 207 毫秒!
            // begin = 2018/03/23 15:16:16.661, end = 2018/03/23 15:16:16.861, elapsed = 200 毫秒!
            // begin = 2018/03/23 15:16:16.745, end = 2018/03/23 15:16:16.945, elapsed = 200 毫秒!
            // 发生了两次重试，consumer_reties = 2生效了，低层次的配置为0时，不会覆盖高层次的重试次数值
            // 第三组，将 reference_timeout 设置为4，判断非0时，是否会发生覆盖
            // 客户端：
            // begin = 2018/03/23 15:23:19.518, end = 2018/03/23 15:23:20.044, elapsed = 526 毫秒!
            // 服务端：
            // begin = 2018/03/23 15:23:19.591, end = 2018/03/23 15:23:19.791, elapsed = 200 毫秒!
            // begin = 2018/03/23 15:23:19.637, end = 2018/03/23 15:23:19.837, elapsed = 200 毫秒!
            // begin = 2018/03/23 15:23:19.741, end = 2018/03/23 15:23:19.941, elapsed = 200 毫秒!
            // begin = 2018/03/23 15:23:19.841, end = 2018/03/23 15:23:20.041, elapsed = 200 毫秒!
            // begin = 2018/03/23 15:23:19.943, end = 2018/03/23 15:23:20.143, elapsed = 200 毫秒!
            // 证实上上面的猜测，当低层次的配置不为0时，会覆盖高层次的设定
            log.info("================> begin = {}, end = {}, elapsed = {} 毫秒!",
                    LocalDateTime.ofInstant(Instant.ofEpochMilli(begin), ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss.SSS")),
                    LocalDateTime.ofInstant(Instant.ofEpochMilli(end), ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss.SSS")),
                    end - begin);
        }
    }

}
