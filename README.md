# Dubbo + Spring Boot

#### 参考资料
- <http://dubbo.apache.org/>
- <http://dubbo.io/>
- <https://projects.spring.io/spring-boot/>

#### 集群容错
- <http://dubbo.io/books/dubbo-user-book/demos/fault-tolerent-strategy.html>
- Failover Cluster
> 失败自动切换，当出现失败，重试其它服务器 1。通常用于读操作，但重试会带来更长延迟。可通过 retries="2" 来设置重试次数(不含第一次)。
- Failfast Cluster
> 快速失败，只发起一次调用，失败立即报错。通常用于非幂等性的写操作，比如新增记录。
- Failsafe Cluster
> 失败安全，出现异常时，直接忽略。通常用于写入审计日志等操作。
- Failback Cluster
> 失败自动恢复，后台记录失败请求，定时重发。通常用于消息通知操作。
- Forking Cluster
> 并行调用多个服务器，只要一个成功即返回。通常用于实时性要求较高的读操作，但需要浪费更多服务资源。可通过 forks="2" 来设置最大并行数。
- Broadcast Cluster
> 广播调用所有提供者，逐个调用，任意一台报错则报错 2。通常用于通知所有提供者更新缓存或日志等本地资源信息。

#### 负载均衡
- <http://dubbo.io/books/dubbo-user-book/demos/loadbalance.html>
- Random LoadBalance
    - 随机，按权重设置随机概率。
    - 在一个截面上碰撞的概率高，但调用量越大分布越均匀，而且按概率使用权重后也比较均匀，有利于动态调整提供者权重。
- RoundRobin LoadBalance
    - 轮循，按公约后的权重设置轮循比率。
    - 存在慢的提供者累积请求的问题，比如：第二台机器很慢，但没挂，当请求调到第二台时就卡在那，久而久之，所有请求都卡在调到第二台上。
- LeastActive LoadBalance
    - 最少活跃调用数，相同活跃数的随机，活跃数指调用前后计数差。
    - 使慢的提供者收到更少请求，因为越慢的提供者的调用前后计数差会越大。
- ConsistentHash LoadBalance
    - 一致性 Hash，相同参数的请求总是发到同一提供者。
    - 当某一台提供者挂时，原本发往该提供者的请求，基于虚拟节点，平摊到其它提供者，不会引起剧烈变动。
    - 算法参见：http://en.wikipedia.org/wiki/Consistent_hashing
    - 缺省只对第一个参数 Hash，如果要修改，请配置 <dubbo:parameter key="hash.arguments" value="0,1" />
    - 缺省用 160 份虚拟节点，如果要修改，请配置 <dubbo:parameter key="hash.nodes" value="320" />
 
#### 技巧Q&A
- 服务消费不区分版本
```
<dubbo:reference id="barService" interface="com.foo.BarService" version="*" />
```
- 分组聚合
> <http://dubbo.io/books/dubbo-user-book/demos/group-merger.html>
- 结果缓存
> <http://dubbo.io/books/dubbo-user-book/demos/result-cache.html>
- 泛化调用
    - <http://dubbo.io/books/dubbo-user-book/demos/generic-service.html>
    - <http://dubbo.io/books/dubbo-user-book/demos/generic-reference.html>
- 回声测试
> <http://dubbo.io/books/dubbo-user-book/demos/echo-service.html>
