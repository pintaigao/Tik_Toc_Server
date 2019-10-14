Redis Bug: 



1. 提示：MISCONF Redis is configured to save RDB snapshots

   解决方式： > redis-cli ---> config set stop-writes-on-bgsave-error no

2. 提示：DENIED Redis is running in protected mode because protected mode is enabled, no bind address was specified, no authentication password is requested to clients. 

   问题：redis 的requirepass 和 Server 配置的password 不一致

   靠谱的解决方案：

    1. redis-cli

    2. 127.0.0.1:6379> config get requirepass 先查看一下目前的password

       > 按理来说应该会提示 ：1）“requirepass”   2 ） ‘’ “

   	3.	127.0.0.1:6379> config set requirepass "admin"

       > 按理来说应该会提示 ：OK

3.查看Redis 数据库中的Key : Value 值， 以key为 user-redis-session:1810244KFCHB2DGC 为一个例子，你觉得是 GET user-redis-session:1810244KFCHB2DGC ？。。。其实是GET "user-redis-session:1810244KFCHB2DGC"， 别忘了双引号，别问我为什么 ！！！！