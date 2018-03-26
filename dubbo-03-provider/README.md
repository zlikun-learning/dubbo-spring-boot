#### 关于集群容错与重试次数一些结论
- 集群容错为`failover`时，将触发重试，由`retries`配置决定
- `retries`配置遵循`dubbo`的配置覆盖策略，但如果任意位置配置为非0值时，后面的默认值和0值都将无效，将以优先级最高的非0值决定重试次数
- 集群容错为`failfast`时，将忽略重试，即：快速失败

#### 关于并发控制的一些结论
- `accepts`、`connections`分别用于控制服务端、客户端的连接数(非并发，可以理解为线程数)，其中`accepts`只能配置于服务端，且`connections`则可同时存在于服务端、客户端，符合`dubbo`的配置覆盖策略，但其值不能大于`accepts`值，否则服务端会抛出异常
```
2018-03-26 11:41:46.420 ERROR 10172 --- [erverWorker-5-5] c.a.d.remoting.transport.AbstractServer  :  [DUBBO] Close channel NettyChannel [channel=[id: 0x24ce7d61, L:/192.168.70.57:20880 - R:/192.168.70.57:57556]], cause: The server /192.168.70.57:20880 connections greater than max config 5, dubbo version: 2.6.1, current host: 192.168.70.57
```
- 