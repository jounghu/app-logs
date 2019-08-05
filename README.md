### 手机App分析系统


#### skrein-common

基本Model，mocklog工具类

#### skrein-collect

Spring Boot + kafka + logback 

[已实现] springboot直接抛送数据进kafka，给后续的skrein-process实时处理。


[已实现] logback生产离线文件，通过crontab倒入hive

> 注意： 代码中，我采用logback的多个不同等级将日志分别输出到不同的目录文件中，实际生产并不可取

#### skrein-process

// TODO...

Spark-Streaming 消费kafka进行实时处理，写入Hbase