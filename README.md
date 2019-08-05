### 手机App分析系统


#### skrein-common

基本Model，mocklog工具类

#### skrein-collect

Spring Boot + kafka + logback 

[已实现] springboot直接抛送数据进kafka，给后续的skrein-process实时处理。

[未实现] logback生产离线文件，通过crontab倒入hive


#### skrein-process

// TODO...

Spark-Streaming 消费kafka进行实时处理，写入Hbase