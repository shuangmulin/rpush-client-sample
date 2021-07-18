# rpush-client-sample
Rpush客户端示例
#### 命令行客户端：
1. 执行：mvn clean compile assembly:single
2. java -jar .\rpush-client-sample-1.0-SNAPSHOT-jar-with-dependencies.jar -ci 【clientId】 -cs 【clientSecret】 -l 【登录名】 -p 【登录密码】 -s 【服务地址】

或者，直接运行com.regent.rpush.client.sample.Main#main方法（运行前先填好参数）

#### web端：直接打开websocket.html即可直接使用
