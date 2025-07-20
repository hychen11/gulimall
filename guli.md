https://www.yuque.com/zhangshuaiyin/guli-mall

https://www.yuque.com/mo_ming/gl7b70/azodep

# Nignx

#### Round Robin

- **How it works**: The Round Robin method distributes client requests across the servers in a sequential manner. Each server gets an equal number of requests by taking turns in a circular order.
- **Advantages**: Simple to implement, good for systems where all servers have similar capacities.
- **Disadvantages**: It does not account for differences in server performance or load, which can lead to inefficiencies if some servers are slower or have more resources than others.

#### Least Connections

- **How it works**: restfultoolkitThe Least Connections strategy sends incoming requests to the server with the fewest active connections. This helps balance the load dynamically based on the actual server load at any given time.
- **Advantages**: Better for environments where connections are long-lived or servers have varying processing capacities.
- **Disadvantages**: Requires tracking active connections, which adds a bit of overhead compared to Round Robin.

#### IP Hash (Source-based Hashing)

- **How it works**: In the IP Hash strategy, the client’s IP address is used to generate a hash, which determines which server will handle the request. This ensures that requests from the same client are consistently routed to the same server.
- **Advantages**: Useful for scenarios where session persistence is important, such as with stateful applications.
- **Disadvantages**: Can lead to unbalanced loads if the client distribution is uneven, and it doesn’t adapt well to servers with varying capabilities.

### Service registration/discovery

when server1 with service B go online->tell nacos-> register

discovery: go nacos, then discover sevice is in server1, server2

### Nacos (Naming Configuration Service)

**service discovery, configuration management, and dynamic DNS** for microservices architectures

supporting both APIs and gRPC for service interaction.

### gRPC

 high-performance, open-source framework developed by Google for **remote procedure calls (RPCs)**

gRPC uses **Protocol Buffers (Protobuf)**, a binary serialization format, for defining service methods and data types, making it more efficient than traditional text-based formats like JSON or XML.

### Configuration Center

get configuration from center (ato configuration)

### Circuit Breaking & Service Downgrading

Breaking: Like a circuit breaker in an electrical system, it temporarily blocks requests to a failing service to prevent overloading. When the service recovers, the circuit breaker "closes" and resumes traffic.

Downgrading: When a service is overloaded or fails, the system reduces its level of service by providing fallback responses, default results, or limiting features to maintain availability.

简单说失败达到阈值，融断，直接不调用这个服务

降级：高峰期，系统资源紧张，非核心服务降级，比如throw exception， return null，mock数据，fallback处理逻辑

### API gateway

all request -> gateway -> final service request!

# IDEA plug

```
restfultoolkit
jrebel
lombok
Mybatis
```

# Redis & Mysql

spring Cloud 微服务

分布式：不同业务不同地方

集群：几台服务器实现同一业务

rpc ->Spring Cloud 中使用HTTP+JSON完成远程调用（不同服务存在不同主机里，服务之间互相调用）

### docker install mysql

```sql
ALTER USER 'root'@'localhost' IDENTIFIED BY '0602';
FLUSH PRIVILEGES;
exit;
```

```shell
(base) ➜  ~ sudo docker pull mysql
(base) ➜  ~ mkdir -p ~/mydata/mysql/log ~/mydata/mysql/data ~/mydata/mysql/conf
(base) ➜  ~ sudo docker run -p 3307:3306 --name docker_mysql \
-v ~/mydata/mysql/log:/var/log/mysql \
-v ~/mydata/mysql/data:/var/lib/mysql \
-v ~/mydata/mysql/conf:/etc/mysql/conf.d \
-e MYSQL_ROOT_PASSWORD=0602 \
-d mysql:latest
(base) ➜  ~ docker ps
```

```shell
cd /mydata/mysql/conf
vi my.cnf

[client]
default-character-set=utf8
[mysql]
default-character-set=utf8
[mysqld]
init_connect='SET collation_connection = utf8_unicode_ci'
init_connect='SET NAMES utf8'
character-set-server=utf8
collation-server=utf8_unicode_ci
#skip-character-set-client-handshake
skip-name-resolve

docker restart mysql
```

hosts 3307 will map to docker 3306, so we can visit host 3307 to visit 3306.

`mysql -u root -p -h 127.0.0.1 -P 3307`

`show tables;`

它给的sql创建文件有问题，要手动加上，不然datagrip会报错（不能用ssh连接docker里的mysql）

```sqlite
CREATE DATABASE /*!32312 IF NOT EXISTS*/`gulimall_ums` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;

USE `gulimall_ums`;
```

`MYSQL_ROOT_PASSWORD` set password as `root`

### docker install redis

```shell
(base) ➜  ~ docker pull redis
(base) ➜  ~ mkdir -p ~/mydata/redis/conf
(base) ➜  ~ touch ~/mydata/redis/conf/redis.conf
(base) ➜  ~ echo "appendonly yes"  >> ~/mydata/redis/conf/redis.conf # for serialize
(base) ➜  ~ docker run -p 6380:6379 --name docker_redis \
-v ~/mydata/redis/data:/data \
-v ~/mydata/redis/conf/redis.conf:/etc/redis/redis.conf \
-d redis redis-server /etc/redis/redis.conf
```

```shell
# mysql
docker update docker_mysql --restart=always
# redis
docker update docker_redis --restart=always
```

两个必要组键

* **Spring Web**
* **OpenFeign**

然后聚合，把不同微服务合在一起

```xml
    <modules>
        <module>gulimall-coupon</module>
        <module>gulimall-member</module>
        <module>gulimall-order</module>
        <module>gulimall-product</module>
        <module>gulimall-ware</module>
    </modules>
```

`.gitignore`

```
**/mvnw
**/mvnw.cmd

**/.mvn
**/target/

.idea
```

# renren opensource(Backend and Frontend)

https://gitee.com/renrenio

renren-fast is backend adminstration system (this is based on springboot 2.6.6, and not 3.X.X, so need old version java 17)

renren-generator 

renren-fast-vue

use jdk 17 and change lomlock version to `<lombok.version>1.18.30</lombok.version>`

```shell
sudo update-alternatives --config java
vim ~/.zshrc
#change $JAVA_HOME
source ~/.zshrc
```

frontend use nvm manage node version!

```shell
(base) ➜  ~ node --version
v14.21.3
```

use the mysql.sql script in renren-fast/db to build admin database

then modify resource/application_dev.yml for jdbc connection

## renren fast

nvm install 14!

```
npm install node-sass@npm:sass --ignore-scripts
npm run dev
```

# generator

```yml
erver:
  port: 30080

# mysql
spring:
  datasource:
    type: com.alibaba.druid.pool.DruidDataSource
    #MySQL配置
    driverClassName: com.mysql.cj.jdbc.Driver
   	url: jdbc:mysql://localhost:3307/gulimall_pms?useUnicode=true&characterEncoding=UTF-8&serverTimezone=America/Chicago
    username: root
    password: "0602"
```

`geenrator.properties`

```properties
#\u4EE3\u7801\u751F\u6210\u5668\uFF0C\u914D\u7F6E\u4FE1\u606F

mainPath=com.hychen11
#\u5305\u540D
package=com.hychen11.gulimall
moduleName=product
#\u4F5C\u8005
author=hychen
#Email
email=zjuchy1@gmail.com
#\u8868\u524D\u7F00(\u7C7B\u540D\u4E0D\u4F1A\u5305\u542B\u8868\u524D\u7F00)
tablePrefix=pms_
```

Copy main into dir

template controller delete `RequiresPermissions`

# gulimall-common

because generator has many DAO,DTO,entity  error, so need to create a class, that contains these class

so add pom.xml (product)

```xml
 <dependency>
      <groupId>com.hychen11</groupId>
      <artifactId>common</artifactId>
      <version>0.0.1-SNAPSHOT</version>
  </dependency>
```



```java
/**
 * 1、整合MyBatis-Plus
 *      1）、导入依赖
 *     <dependency>
            <groupId>com.baomidou</groupId>
            <artifactId>mybatis-plus-boot-starter</artifactId>
            <version>3.5.10.1</version>
        </dependency>
 *      2）、配置
 *          1、配置数据源；
 *              1）、导入数据库的驱动。https://dev.mysql.com/doc/connector-j/8.0/en/connector-j-versions.html
 *              2）、在application.yml配置数据源相关信息
 *          2、配置MyBatis-Plus；
 *              1）、使用@MapperScan
 *              2）、告诉MyBatis-Plus，sql映射文件位置
 *
 * 2、逻辑删除
 *  1）、配置全局的逻辑删除规则（省略）
 *  2）、配置逻辑删除的组件Bean（省略）
 *  3）、给Bean加上逻辑删除注解@TableLogic
 *
 * 3、JSR303
 *   1）、给Bean添加校验注解:javax.validation.constraints，并定义自己的message提示
 *   2)、开启校验功能@Valid
 *      效果：校验错误以后会有默认的响应；
 *   3）、给校验的bean后紧跟一个BindingResult，就可以获取到校验的结果
 *   4）、分组校验（多场景的复杂校验）
 *         1)、	@NotBlank(message = "品牌名必须提交",groups = {AddGroup.class,UpdateGroup.class})
 *          给校验注解标注什么情况需要进行校验
 *         2）、@Validated({AddGroup.class})
 *         3)、默认没有指定分组的校验注解@NotBlank，在分组校验情况@Validated({AddGroup.class})下不生效，只会在@Validated生效；
 *
 *   5）、自定义校验
 *      1）、编写一个自定义的校验注解
 *      2）、编写一个自定义的校验器 ConstraintValidator
 *      3）、关联自定义的校验器和自定义的校验注解
 *
 * 4、统一的异常处理
 * @ControllerAdvice
 *  1）、编写异常处理类，使用@ControllerAdvice。
 *  2）、使用@ExceptionHandler标注方法可以处理的异常。
 */
```

step1 导入依赖

加入utils 的`Constant`，`PageUtils`，`Query`，`R`和xss `SQLFilter`

相当于补全报错

然后pom.xml加入dependency

```xml
		<!--mybatis plus-->
		<dependency>
			<groupId>com.baomidou</groupId>
			<artifactId>mybatis-plus-boot-starter</artifactId>
			<version>3.2.0</version>
		</dependency>

		<dependency>
			<groupId>org.projectlombok</groupId>
			<artifactId>lombok</artifactId>
			<version>1.18.8</version>
		</dependency>
 
    <dependency>
			<groupId>org.apache.httpcomponents</groupId>
			<artifactId>httpcore</artifactId>
			<version>4.4.16</version>
			<scope>compile</scope>
		</dependency>
```

step2.1 配置数据源

导入数据库驱动

```xml
<!--		import mysql-->
		<dependency>
			<groupId>mysql</groupId>
			<artifactId>mysql-connector-java</artifactId>
			<version>8.0.30</version>
		</dependency>
		<dependency>
			<groupId>javax.servlet</groupId>
			<artifactId>servlet-api</artifactId>
			<version>2.3</version>
			<scope>provided</scope>
		</dependency>
```

在application.yml里配置数据源，表述数据操作到哪个表里

```yml
spring:
  datasource:
    driverClassName: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3307/gulimall_pms?useUnicode=true&characterEncoding=UTF-8&serverTimezone=America/Chicago
    username: root
    password: "root"
```

step2.2 配置Mybatis-plus

使用@MapperScan

在启动项加上

```java
import org.mybatis.spring.annotation.MapperScan;
@SpringBootApplication
@MapperScan("com.hychen11.gulimall.product.dao")
```

MapperPath

告诉MyBatis-Plus，sql映射文件位置

```yml
mybatis-plus:
  mapper-locations: classpath:/mapper/**/*.xml
  global-config:
    db-config:
      id-type: auto
```

entity上有`@TableId`表示这个是主键，`auto`表示自增主键 

mysql的索引是b+树结构，如果用uuid b+树结构会不停变化很消耗资源，一般都是自增

然后运行报错`java.lang.IllegalStateException: Failed to load ApplicationContext`

mvn idea:module 命令执行完毕后生成iml文件，再次点击maven build工程顺利编译通过

### 设定端口

```yml
server:
	port:7000
```

coupon: 7000

member: 8000

order: 9000

product: 10000

ware: 11000

# SpringCloud

register center

config center

gateway

![image.png](https://cdn.nlark.com/yuque/0/2021/png/12568777/1615002771822-1f445dfe-5b23-4ab2-b37c-f60946058944.png?x-oss-process=image%2Fwatermark%2Ctype_d3F5LW1pY3JvaGVp%2Csize_59%2Ctext_RXDmtYHoi48%3D%2Ccolor_FFFFFF%2Cshadow_50%2Ct_80%2Cg_se%2Cx_10%2Cy_10%2Fformat%2Cwebp%2Fresize%2Cw_750%2Climit_0)

SpringCloud Alibaba Nacos: Register center, Config center

SpringCloud Ribbon (load balance) 

SpringCloud Feign: RPC

**SpringCloud Alibaba Sentinel** (Fault Tolerance, Rate Limiting or Throttling, Graceful Degradation or Fallback, Circuit Breaker) 服务容错限流降级融断

SpringCloud gateway API gateway webflux

SpringCloud Sleuth

SpringCloud Alibaba -seata (Fescar)

### SpringCloud Alibaba version

springboot version 2.6.6, jdk17.

gulimall-common `pom.xml`

```xml
<dependencies>
    <dependency>
        <groupId>com.alibaba.cloud</groupId>
        <artifactId>spring-cloud-alibaba-dependencies</artifactId>
        <version>2021.0.1.0</version>
        <type>pom</type>
        <scope>import</scope>
    </dependency>
</dependencies>
```

**2021.x branch**: Corresponds to Spring Cloud 2021 & Spring Boot 2.6.x. JDK 1.8 or later versions are supported.

### Nacos register

download nacos https://github.com/alibaba/nacos/releases

https://github.com/alibaba/spring-cloud-alibaba/blob/2023.x/spring-cloud-alibaba-examples/nacos-example/readme.md

#### docker Nacos

```shell
(base) ➜  ~ docker run --name nacos -d -p 8849:8848 \                  
--restart=always \
-e JVM_XMS=512m \
-e JVM_XMX=2048m \
-e MODE=standalone \
-e PREFER_HOST_MODE=hostname \
-v ~/nacos-logs:/home/nacos/logs \    
nacos/nacos-server:latest

(base) ➜  ~ docker update nacos --restart=always
```



**服务注册发现**也放gulimall-common pom.xml里

Before launching the Nacos Discovery sample for demonstration, take a look at how Spring Cloud applications access Nacos Discovery.

**Note that this section is only for your convenience to understand the access method. The access work has been completed in this sample code, and you do not need to modify it.**

1. First, modify the `pom.xml` file and introduce spring-cloud-alibaba-nacos-discovery-starter;

   ```xml
   <dependency>
       <groupId>com.alibaba.cloud</groupId>
       <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
   </dependency>
   ```

2. Configuring a Nacos Server address in an `/src/main/resources/application.properties` applied configuration file;

   ```properties
   spring.cloud.nacos.discovery.server-addr=127.0.0.1:8848
   spring.application.name: gulimall-XXX
   #注意这里nacos 2.5要带上username, password, default is nacos/nacos
   
     cloud:
       nacos:
         discovery:
           server-addr: localhost:8849
           username: nacos
           password: "nacos"
     application:
       name: product
   ```

3. Use `@ EnableDiscoveryClient` annotation to enable service registration and discovery;

   ```java
   @SpringBootApplication
   @EnableDiscoveryClient
   public class ProviderApplication {
   
       public static void main(String[] args) {
           SpringApplication.run(ProviderApplication.class, args);
       }
   
       @RestController
       class EchoController {
           @GetMapping(value = "/echo/{string}")
           public String echo(@PathVariable String string) {
                   return string;
           }
       }
   }
   ```

```
cd nacos/bin
bash startup.sh -m standalone
```

`-m standalone` means Singleton Pattern

check `localhost:8848/nacos`

self-start

```shell
sudo nano /etc/systemd/system/nacos.service


[Unit]
Description=Nacos Server
After=network.target

[Service]
Environment="JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64/bin/java"
Type=forking
ExecStart=/opt/nacos/bin/startup.sh -m standalone
ExecStop=/opt/nacos/bin/shutdown.sh
Restart=on-failure
LimitNOFILE=65535
PrivateTmp=true
[Install]
WantedBy=multi-user.target


sudo systemctl daemon-reload 
systemctl start nacos
systemctl status nacos
systemctl stop nacos
systemctl enable nacos
systemctl is-enabled nacos
```

简单说就是**加依赖**，**加注解`@EnableDiscoveryClient`**，**加application.yml**

> 用于在 Spring Cloud 项目中启用服务注册和发现功能。它的主要作用是使应用程序能够注册到服务注册中心（比如 Eureka、Consul 或 Zookeeper），从而实现微服务的自动发现和负载均衡等功能。
>
> “注册到 Eureka”指的是当一个微服务启动时，它会自动将自己的信息（如服务名称、IP 地址、端口号等）发送到 Eureka 服务器（也就是注册中心）。这样，Eureka 注册中心就会记录下该服务的实例信息，以便其他服务可以通过 Eureka 发现并调用它。

### Feign

> **Process: service A want call service B, then visit nacos find B on machine1,2,3, return one machine to service A, then it can call**

Feign is Declarative HTTP Client

Feign 允许开发者定义接口，并通过注解方式指定接口方法要请求的 HTTP 路径、参数等。Feign 会根据接口和注解自动生成对应的 HTTP 请求逻辑，并通过注册中心查找服务地址，简化了服务调用。

**声明式接口**：你只需定义一个 Java 接口，并用注解来描述 HTTP 请求的细节，比如 `@GetMapping`、`@PostMapping` 等。

**自动代理**：Feign 会在运行时创建接口的代理，实现远程调用。通过代理对象，Feign 会自动生成 HTTP 请求，发送到目标服务，并将返回结果映射为 Java 对象。

**负载均衡**：结合 Eureka 和 **Ribbon**（已内置在 Spring Cloud Feign 中），Feign 支持客户端负载均衡，可以自动选择一个可用的实例来发送请求。

有一个 `UserService`，另一个服务 `OrderService` 想调用它的 `/user/info` 接口。可以这样定义 Feign 客户端接口：

`@FeignClient(name = "coupon", url ="http://localhost:7000")`如果未指定 url，会根据 name 在 Eureka/Nacos 等注册中心寻找 coupon 服务

```java
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "UserService")  // 表示调用的服务名
public interface UserClient {

    @GetMapping("/user/info")  // 定义请求路径
    UserInfo getUserInfo(@RequestParam("id") Long userId);
}
```

在 `OrderService` 中注入 `UserClient` 接口后，就可以像调用本地方法一样调用 `getUserInfo` 方法，而 Feign 会自动发出 HTTP 请求到 `UserService` 的 `/user/info`

#### openfeign

```java
/*
    1）include openfeign
    2）interface, tell springcloud this interface needs rpc
    3) open rpc (@EnableFeignClients)
*/
```

1） 在创建module里就勾了springweb+openfeign

```xml
       <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-openfeign</artifactId>
        </dependency>
```

2）创建一个feign的package，创建 的Interface

```java
package com.hychen11.gulimall.member.feign;

import com.hychen11.gulimall.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name="coupon",url = "http://localhost:7000")
public interface CouponFeignService {
    @GetMapping("/coupon/coupon/member/list")
    public R memberCoupons();
}
```

注意，这里的R class相当与把数据Map成JSON格式，然后返回给客户端

如果调用接口的`memberCoupons()`,去注册中心找`coupon`，然后调用请求`/coupon/coupon/member/list`

3）Main Application Class add @EnableFeignClients

```java
@EnableFeignClients(basePackages = "com.hychen11.gulimall.member.feign")
```

启动时会自动扫描这个路径下的所有标了`@FeignClient` annotation的 Interface

> 回顾一下feign的调用流程，假设service A，service B, A 有funcA，B想调用A的funcA
>
> Step1 B创建feign package，然后在Application加上`@EnableFeignClients(basePackages="")`在启动时可以读取到feign包的内容
>
> Step2 B的feign package里有AFeignService interface，里面函数就是直接copy A的funcA，然后还要在头上加上`@FeignClient(A)` 为了去nacos找到service A的machine地址然后通过http发送请求得到回复，然后这里的`RequestMapping` 要包含完整路径
>
> Step3 注入AFeignService，调用函数
>
> ```
> Service B 调用 AFeignService.funcA()
> → Feign 代理 HTTP 请求
> → Nacos 获取 Service A 地址
> → 发送 HTTP 请求到 Service A
> → Service A 处理并返回数据
> → Feign 解析 JSON，返回 Java 对象
> ```

### Nacos config

step1 配置中心做配置管理

add into gulimall-common pom.xml

```xml
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-nacos-config</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-bootstrap</artifactId>
    <version>3.0.2</version>
</dependency>
```

step2

创建`bootstrap.properties`

```properties
spring.application.name=gulimall-coupon
spring.cloud.nacos.config.server-addr=127.0.0.1:8848
```

step3

`application.properties`适用于静态值的读取

```
coupons.user.name="b"
```

 ```java
 @Value("${coupons.user.name}")
 private String name;
 ```

step4

动态更新 在nacos里的Edit Configuration里properties添加

这里在controller.java里加上动态刷新的annotation

```java
@RefreshScope
```

step5

这里不能刷新，在application.yml里加上，nacos上也要在`gulimall-coupon.properties`这里加上配置

```yml
  cloud:
    nacos:
      serverAddr: 127.0.0.1:8848
      config:
        preference: remote
      discovery:
        server-addr: localhost:8848

  config:
    import:
      - nacos:gulimall-coupon.properties?refreshEnabled=true&group=DEFAULT_GROUP
```

### Namespace

default is in public

We can also have (every microserver create their own namespace (ioslation))

in application.yml add  `namespace: 6b8fe1af-0411-4ca5-a000-eef67eafc137` and ` group: coupon`

```yml
  cloud:
    nacos:
      serverAddr: 127.0.0.1:8848
      namespace: 6b8fe1af-0411-4ca5-a000-eef67eafc137
      group: coupon
      config:
        preference: remote
      discovery:
        server-addr: localhost:8848
```

# Gateway

nginx is one kind of gateway

> Nginx 本质上是一个**高性能的 HTTP 服务器和反向代理**，而 API Gateway 是一种专门针对**微服务架构**的网关解决方案

Client -> **API-service(gateway)**->{user-service,good-service,...}

Zuui RPS(20000), SpringCloud gateway RPS(30000)

### Features

Spring Cloud Gateway features:

- Built on Spring Framework and Spring Boot
- Able to match routes on any request attribute.
- Predicates and filters are specific to routes.
- Circuit Breaker integration.
- Spring Cloud DiscoveryClient integration
- Easy to write **Predicates and Filters**
- **Request Rate Limiting**
- Path Rewriting

URL(Uniform Resource Locator) and URI(Uniform Resource Identifier)

uri身份证号 url地址定位+身份证

### Glossary

- **Route**: The basic building block of the gateway. It is defined by an ID, a destination URI, a collection of predicates, and a collection of filters. **A route is matched if the aggregate predicate is true.**

- **Predicate**: This is a [Java 8 Function Predicate](https://docs.oracle.com/javase/8/docs/api/java/util/function/Predicate.html). The input type is a [Spring Framework `ServerWebExchange`](https://docs.spring.io/spring/docs/5.0.x/javadoc-api/org/springframework/web/server/ServerWebExchange.html). This lets you match on anything from the HTTP request, such as headers or parameters.

  > whether Routing and Forwarding needs condition judge (Predicate)

- **Filter**: These are instances of [`GatewayFilter`](https://github.com/spring-cloud/spring-cloud-gateway/blob/main/spring-cloud-gateway-server/src/main/java/org/springframework/cloud/gateway/filter/GatewayFilter.java) that have been constructed with a specific factory. Here, you can modify **requests and responses** before or after sending the downstream request.

  > filter 

**断言是用来匹配的,匹配成功就发送请求,fileter是拦截请求的**

我们可以定义一个过滤器来拦截所有来源IP不在白名单中的请求，或者对所有请求进行日志记录，并根据请求中携带的自定义标记进行不同的服务映射

Whitelist是一组被授权或允许访问的资源或服务的 IP 地址或用户列表

![](https://docs.spring.io/spring-cloud-gateway/reference/_images/spring_cloud_gateway_diagram.png)

Clients make requests to Spring Cloud Gateway. If the Gateway Handler Mapping determines that a request matches a route, it is sent to the Gateway Web Handler. This handler runs the request through a filter chain that is specific to the request. The reason the filters are divided by the dotted line is that filters can run logic both before and after the proxy request is sent. All “pre” filter logic is executed. Then the proxy request is made. After the proxy request is made, the “post” filter logic is run.

URIs defined in routes without a port get default port values of **80 and 443 for the HTTP and HTTPS** URIs, respectively.

https://docs.spring.io/spring-cloud-gateway/reference/spring-cloud-gateway/request-predicates-factories.html

step1）开启服务注册发现`@EnableDiscoveryClient`

因为引入了common，里面有mybatis的操作，需要有数据源，但是这里用不到，就直接排除

`@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})`

**`bootstrap.properties`**最早加载，初始化springcloud组件

```properties
spring.application.name = gateway
spring.cloud.nacos.config.server-addr = 127.0.0.1:8849
spring.cloud.nacos.discovery.namespace = 41e93e2f-de27-4db9-80b9-8ae0fc7e3634
```

`application.properties` 后加载读取

```yml
spring.application.name=gateway
spring.cloud.nacos.discovery.server-addr=127.0.0.1:8849
server.port= 12000
```

Netty started on port 12000

Query注意这里要 `http://localhost:12000/?url=google` 这里`?`才是query！才能被perdicate

`？`前的是查询的地址，比如`http://localhost:12000/hello?url=google`就是查询`https://www.google.com/hello`

```yml
spring:
  cloud:
    gateway:
      routes:
        - id: test_route
          uri: https://www.google.com
          predicates:
            - Query=url,google
        - id: baidu_route
          uri: https://www.baidu.com
          predicates:
            - Query=url,baidu
```

# Dubbo

**服务暴露**：服务提供者（Provider）将服务接口暴露出去，并注册到注册中心，注册中心会记录该服务的信息（如 IP、端口）。

**服务引用**：服务消费者（Consumer）从注册中心获取服务地址，并通过 Dubbo 生成的代理类发起 RPC 请求调用服务提供者的方法。

**调用链路**：Dubbo 会根据注册中心的信息、负载均衡策略，将调用请求转发到合适的服务实例，实现了客户端和服务端的解耦。

```java
// 服务接口定义
public interface HelloService {
    String sayHello(String name);
}

// 服务实现
public class HelloServiceImpl implements HelloService {
    public String sayHello(String name) {
        return "Hello " + name;
    }
}
```

```
<!-- 服务提供者的配置 -->
<dubbo:application name="provider-app" />
<dubbo:registry address="zookeeper://localhost:2181" />
<dubbo:protocol name="dubbo" port="20880" />
<dubbo:service interface="com.example.HelloService" ref="helloService" />

<bean id="helloService" class="com.example.HelloServiceImpl" />
```

```
<!-- 服务消费者的配置 -->
<dubbo:application name="consumer-app" />
<dubbo:registry address="zookeeper://localhost:2181" />
<dubbo:reference id="helloService" interface="com.example.HelloService" />

// 使用服务
HelloService helloService = (HelloService) context.getBean("helloService");
String result = helloService.sayHello("Dubbo");
```

# 三级API 2.14

`protected`

**同一个类（自身）**

**同一个包（package）内的所有类**

**不同包的子类（`extends`）**

```java
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {}
//这里Dao和Entity被封装在ServiceImpl的M和T里
//或者直接@Autowire注入
```

## Stream & Filter & Collect

stream可以处理数据集合

`filter()` 是 **Stream** 的 **中间操作**，用于**筛选元素**，保留满足条件的元素，丢弃不符合条件的

`filter()` 方法接受一个 **Lambda 表达式**，该表达式返回 `true` 表示保留元素，返回 `false` 表示丢弃

`.collect(Collectors.toList())`：将流的结果收集为 `List`

` .map(String::toUpperCase)` 

```java
List<CategoryEntity> collect = categoryEntities.stream().filter((entity) -> {
    return entity.getParentCid() == 0;
}).collect(Collectors.toList());
```

## Lambda

```java
(parameters) -> expression
(parameters) -> { statements; } //(parameters) -> { return expression; }
```

parameters可为空

## `@TableField`

**映射 Java 类的字段到数据库表的列**，可以控制字段与数据库表的关系，例如：

- 指定表字段名称
- 处理数据库中不存在的字段
- 处理数据库中的额外字段
- 忽略某些字段

```java
	//在CategoryEntity里加入
	@TableField(exist = false)
	private List<CategoryEntity> child;
```

## recursive search

```java
private List<CategoryEntity> getChild(CategoryEntity root, List<CategoryEntity> all) {
    List<CategoryEntity> collect = all.stream()
            .filter(entity -> entity.getParentCid() == root.getCatId())
            .map(menu -> {menu.setChild(getChild(menu, all)); return menu;})
            .collect(Collectors.toList());
    return collect;
}
```

核心逻辑就是这里的root父级，child级，filter出所有patentid==catid的entities，然后再getChild，用map设置child

## config vue template

添加一级菜单和二级菜单，我们设置的url `product/category`这里router显示规则是`product-category`

`views/modules/sys/`里有sys-role页面，那么就创建`views/modules/product/category.vue`

install plugins `Vetur` , vue template https://www.cnblogs.com/songjilong/p/12635448.html

## 配置网关与路由重写!!

```vue
this.$http({
  url: this.$http.adornUrl("/product/category/list/tree"),
  method: "get",
}).then((data)=>{});
```

这里需要访问 `http://localhost:10000/product/category/list/tree`

也就是`index.js`里

```js
/**
 * 开发环境
 */
;(function () {
  window.SITE_CONFIG = {};

  // api接口请求地址
  window.SITE_CONFIG['baseUrl'] = 'http://localhost:12000/api';

  // cdn地址 = 域名 + 版本号
  window.SITE_CONFIG['domain']  = './'; // 域名
  window.SITE_CONFIG['version'] = '';   // 版本号(年月日时分)
  window.SITE_CONFIG['cdnUrl']  = window.SITE_CONFIG.domain + window.SITE_CONFIG.version;
})();
```

发送到网关，renren-fast发送验证码，也要**加入nacos**

gateway的application.yml里加入

```yml
- id: admin_route
  uri: lb://renren-fast
  ## lb is load balance，使用loadbalance，并且把请求转发到renren-fast服务
  ## lb:// 表示这是一个通过注册中心（如 Nacos、Eureka）进行服务发现的负载均衡调用
  predicates:
    ## 前端项目发送请求，带有/api前缀
    - Path=/api/**
  filters:
  	- RewritePath=/api/(?<segment>.*),/renren-fast/$\{segment}
  	## 将api前缀替换为renren-fast前缀
```

`uri: lb://renren-fast` 就是请求来了，先去nacos里找renren-fast到服务，发现servers后进行lb 负载均衡转发请求，

断言规则（Predicates）：前端向网关发起请求，只有携带`/api`关键字才将请求通过负载均衡的方式转发给`renren-fast`

`  window.SITE_CONFIG['baseUrl'] = 'http://localhost:12000/api';`

因此发送`http://localhost:8080/api/1`可以转发到`http://localhost:8080/renren-fast/1`

这里gateway需要和renren-fast同一个name space，不然会有503的error问题

`filters` 解释一下正则匹配到规则：^匹配开头 $匹配结尾

`.`：匹配任意一个字符（除了换行符）。

`*`：匹配前一个字符 0 次或多次。

所以 `.*` 表示：**匹配任意长度的任意字符（包括空字符串）**

`(?<name>pattern)` 是 Java 正则表达式的语法，表示把匹配到的 `pattern` 保存到变量名 `name` 中，后续使用就直接`${name}` 这个是捕获的用法

`(?<segment>.*)`就是匹配任意的放在segment里

## Cross-Origin Resource Sharing，CORS 跨域问题

浏览器出于同源策略（Same-Origin Policy, SOP）的安全限制，阻止前端网页对不同源（不同域名、协议或端口）的资源进行访问

当你的前端代码向不同源的服务器发送请求时，如果服务器没有正确配置 CORS，浏览器会**拦截**这个请求，从而导致跨域问题。

origin = `协议（scheme） + 域名（host） + 端口（port）`

**简单跨域请求**（GET/POST 且无特殊 header）—— 浏览器自动发起请求，但如果响应头中没有 CORS 标志，**JS 无法读取响应内容**。

**复杂跨域请求**（PUT/DELETE、自定义 header）—— 浏览器会先发一个 **预检请求（OPTIONS）** 来询问服务器是否允许跨域。

**同源**指的是：

- **协议**相同（http / https）
- **域名**相同（example.com）
- **端口**相同（80 / 443 / 8080 等）

端口及以前都严格相同

同源策略主要**限制**：

1. **XHR（XMLHttpRequest）和 Fetch 请求**：禁止跨域请求数据。
2. **DOM 访问**：不能操作不同源的 `iframe` 内的 DOM。
3. **Cookie、localStorage、sessionStorage 访问**：不同源的页面不能访问彼此的存储数据

### 跨域的请求方式

- `XMLHttpRequest` 或 `fetch`
- `WebSocket`
- `iframe`（嵌套不同源网页）
- `JSONP`（只适用于 `GET` 请求）

尚未进行配置之前，跨域预检`OPTIONS`都无法通过。

非简单请求要先用`OPTIONS`，响应允许跨域就可以发送真实请求，server再响应数据

#### 方法一

直接让nginx代理跨域

浏览器只会拦截“跨源请求”。如果前端静态资源（HTML/JS/CSS）和后端 API 都是从 **同一个域名+端口+协议** 请求的，那么就 **不算跨域**，浏览器不会阻止。

让前端和后端都通过 Nginx 来服务

```nginx
server {
    listen 80;
    server_name mydomain.com;

    # 前端资源的路由
    location / {
        root /usr/share/nginx/html/renren-fast-vue;
        index index.html;
        try_files $uri $uri/ /index.html;
    }

    # 后端 API 的路由
    location /api/ {
        proxy_pass http://localhost:8080/renren-fast/;
        proxy_set_header Host $host;
    }
}
```

#### 方法二

`Access-Control-Allow-Origin: *` 但是不安全

开发期间，在网关中加入一个`filters`过滤器，通过`配置当此请求允许跨域`的方法来解决跨域问题

```java
@Configuration
public class MallCorsConfiguration {
    @Bean
    public CorsWebFilter corsWebFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        // 1. 配置跨域
        // 1.1 设置允许跨域头部 —— *为所有
        corsConfiguration.addAllowedHeader("*");
        // 1.2 设置允许请求方式 —— *为所有种类请求
        corsConfiguration.addAllowedMethod("*");
        // 1.3 设置允许请求来源 —— *为任意请求来源
//        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.setAllowedOriginPatterns(List.of("*"));
        // 1.4 是否允许携带cookie进行跨域
        corsConfiguration.setAllowCredentials(true);
        source.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsWebFilter(source);
    }
}
```

然后comment掉renren-fast里的corsconfig

规定所有与商品服务有关请求都以**/api/product/**开头

gateway规则正则表达式

```yaml
filters:
  - RewritePath=/api/(?<segment>.*),/$\{segment}
```

`(?<segment>.*)` ：匹配 `/api/` 之后的所有字符，并将其**存入 `segment` 变量**。

`$\{segment}` 代表前面正则捕获的内容（即 `/api/` 之后的部分）

`$\{}` 形式是 **变量占位符**

**高精度匹配路由放在前面，模糊匹配路由放在后面**

```yaml
  - id: product_route
    uri: lb://product
    predicates:
      - Path=/api/product/**
    filters:
      - RewritePath=/api/(?<segment>.*),/$\{segment}
```

这个就是`/api/product/**`改写规则，转发到`product`

这里`lb://product`是目标的地址，通过nacos找到的

### 前端

```vue
<template>
  <el-tree
    :data="menus"
    :props="defaultProps"
    @node-click="handleNodeClick"
  ></el-tree>
</template>

<script>
export default {
  components: {},
  data () {
    return {
      menus: [],
      defaultProps: {
        children: 'child',
        label: 'name'
      }
    }
  },
  methods: {
    handleNodeClick (data) {
      console.log(data)
    },
    getMenu () {
      this.$http({
        url: this.$http.adornUrl('/product/category/list/tree'),
        method: 'get'
      }).then((response) => {
        this.menus = response.data.listWithTree
      })
    }
  },
  created () {
    this.getMenu()
  },
  mounted () {},
  beforeCreate () {},
  beforeMount () {},
  beforeUpdate () {},
  updated () {},
  beforeDestroy () {},
  destroyed () {},
  activated () {}
}
</script>

<style scoped>
</style>
```

getMenu就是发送请求得到response，然后把response.data里的listWithTree存到menus里，然后根据这里tree结构`child`

### Delete

`@RequestMapping` 默认支持**所有 HTTP 方法**，但如果没有显式指定 `method`，Spring 会接受所有类型的请求（`GET`、`POST`、`PUT`、`DELETE` 等）

`@RequestBody` 是 **Spring MVC** 提供的一个注解，用于**将 HTTP 请求的 JSON 数据转换为 Java 对象**，主要用于 `POST`/`PUT` 请求，这里必须发送POST请求！！！！

删除之前，需要检查当前删除的菜单，是否被别的地方引用

- 物理删除：在数据库中将满足条件的数据删除，删除了数据就不存在了
- 逻辑删除：在数据库中使用某一个字段作为标识类，表示是否被删除

```yml
mybatis-plus:
  global-config:
    db-config:
      logic-delete-field: flag  # 全局逻辑删除的实体字段名(since 3.3.0,配置后可以忽略不配置步骤2)
      logic-delete-value: 1 # 逻辑已删除值(默认为 1)
      logic-not-delete-value: 0 # 逻辑未删除值(默认为 0)
```

实体类字段加上`@TableLogic`逻辑删除注解，可以自定义

```java
@TableLogic(value = "1", delval = "0")
private Integer showStatus;
```

然后就是一堆前端的东西了

```vue
remove(node, data) {
    // 1. 获取点击ID
    var ids = [data.catId];
    // 2. 发起POST请求，调用接口删除数据
    this.$http({
      url: this.$http.adornUrl("/product/category/delete"),
      method: "post",
      data: this.$http.adornData(ids, false),
    }).then(({ data }) => {
    // 3. 请求成功后，重新请求所有菜单并显示。
    	this.getMenu();
    });
},
```

然后后面append，edit啥的，全是前端的内容，这里就跳过了

# 品牌管理

OSS的内容先跳过了

JRS303

```java
/**
 * 保存
 */
@RequestMapping("/save")
public R save(@Valid @RequestBody BrandEntity brand, BindingResult result) {
    if (result.hasErrors()) {
        Map<String, String> map = new HashMap<>();
        // 1. 获取校验的错误结果
        result.getFieldErrors().forEach(item -> {
            // FieldError 获取错误提示
            String defaultMessage = item.getDefaultMessage();
            // 获取错误属性名称
            String field = item.getField();
            map.put(field, defaultMessage);
        });
        return R.error(400, "提交的数据不合法").put("data", map);
    } else {
        brandService.save(brand);
        return R.ok();
    }
}
```

统一异常处理

使用`@ControllerAdvice`注解标识

使用`basePackages`标识哪个位置出现异常进行处理

接口使用`BindingResult`会接收错误，感应异常。

删除掉后就不再对异常进行处理，而是直接抛出异常。

`GulimallExceptionControllerAdvice`的作用就是感应异常，集中处理。

错误信息以`JSON`格式返回，需要给类添加`@ResponseBody`注解

`@ResponseBody`注解和`@ControllerAdvice(basePackages`注解可以合并为`@RestControllerAdvice`注解

```java
@ResponseBody
@ControllerAdvice(basePackages = "com/hychen11/product/controller")
public class GulimallExceptionControllerAdvice {
}
```

或者

```java
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = "com/hychen11/product/controller")
public class GulimallExceptionControllerAdvice {
}
```

统一处理`MethodArgumentNotValidException`异常

```java
@Slf4j
@RestControllerAdvice(basePackages = "com/hychen11/product/controller")
public class GulimallExceptionControllerAdvice {
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public R handleVaildException(MethodArgumentNotValidException e) {
        log.error("数据校验出现问题:{},异常类型:{}", e.getMessage(), e.getClass());
        BindingResult bindingResult = e.getBindingResult();
        Map<String, String> errorMap = new HashMap<>();
        bindingResult.getFieldErrors().forEach(fieldError -> {
            errorMap.put(fieldError.getField(), fieldError.getDefaultMessage());
        });
        return R.error(400, "数据校验出现问题").put("data", errorMap);
    }

    // 无法准确匹配后处理
    @ExceptionHandler(value = Throwable.class)
    public R handleException(Throwable throwable) {
        return R.error();
    }
}
```

全局状态码枚举类

**错误码和错误信息定义类**

- 错误码定义规则为 5 为数字

- 前两位表示业务场景，最后三位表示错误码。例如：100001。10:通用 001:系统未知异常
- 维护错误码后需要维护错误描述，将他们定义为枚举形式

10 通用，11商品，12订单，13购物车，14物流

```java
return R.error(BizCodeEnume.VAILE_EXCEPTION.getCode(), BizCodeEnume.VAILE_EXCEPTION.getMsg()).put("data", errorMap);
```

JSR303分组校验

- 新增品牌的时候，由于ID是自动生成的自增长ID，所以新增的时候不携带ID
- 修改品牌的时候，需要根据ID进行修改

有不同的校验场景

entity里

```java
@NotNull(message = "修改必须指定品牌id", groups = {UpdateGroup.class})
@NotBlank(message = "品牌名必须提交", groups = {UpdateGroup.class, AddGroup.class})
@TableId
private Long brandId;
```

Controller 里

```java
@RequestMapping("/save")
public R save(@Validated({AddGroup.class}) @RequestBody BrandEntity brand/*, BindingResult result*/) {
    brandService.save(brand);
    return R.ok();
}
```

默认不分组的话有@Validated不会生效，只有不分组才生效，比如 `@NotEmpty`

自定义校验

1. 编写一个自定义的校验注解
2. 编写一个自定义的校验器
3. 关联自定义的校验器和自定义的校验注解 `让校验器校验校验注解标识的字段`

Annotation的

注解必须拥有三个属性

- message：当校验出错后，错误信息去哪取
- groups：支持分组校验
- payload：自定义负载信息

注解必须有以下原信息数据

```java
@Documented
@Constraint(validatedBy = {ListValueConstraintValidator.class})
@Target({ElementType.METHOD,ElementType.FIELD,ElementType.ANNOTATION_TYPE,ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
```

- Target：注解可以标注在哪些位置
- Retention：时机，可以在运行时获取到
- Constraint：注解使用哪个校验器进行校验，可以指定校验器

创建校验器类文件`ListValueConstraintValidator.java`

`ListValueConstraintValidator`实现接口`ConstraintValidator`

`ConstraintValidator`接口包含两个泛型，第一个为对应注解，第二个为校验数据类型

`initialize`初始化方法 参数`constraintAnnotation`包含默认合法的值

`isValid`校验方法 参数`integer`为提交过来需要检验的值

```java
package com.atguigu.common.valid;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashSet;
import java.util.Set;

public class ListValueConstraintValidator implements ConstraintValidator<ListValue, Integer> {

    private Set<Integer> set = new HashSet<>();

    // 初始化方法
    @Override
    public void initialize(ListValue constraintAnnotation) {
        // 合法的值
        int[] values = constraintAnnotation.values();
        // 将合法值全部放到set中，便于查找是否存在
        for (int value : values) {
            set.add(value);
        }
    }

    // 判断是否校验成功
    @Override
    /*
     *
     * @params value 提交过来需要检验的值
     * @params context 校验的上下文环境信息
     * @return
     */
    public boolean isValid(Integer value, ConstraintValidatorContext constraintValidatorContext) {
        return set.contains(value);
    }
}

```

#### 分组校验总结一下

这里定义好分组接口

```java
public interface AddGroup {}
public interface UpdateGroup {}
```

不需要写别的内容

随后在Entity里定义不同的groups

Controller中指定校验分组 @Validated({XXX.class})，注意这里@Valid是不分组的

# API属性分类+平台属性 

**SPU：Standard Product Unit**

就比如MacBook air

**SKU：Stock Keeping Unit**

就比如带配置的macbook air，13寸，16+256

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/4b0e6e36ba48e4c6542ec38dc13445b3.png)

三级分类树很多地方都要使用，因此新建common文件夹，存放公共组件

子组件：`category.vue`

```vue
this.$emit("event-name",data1,data2...)
nodeclick(data, node, component) {
    //向父组件发送事件；
    this.$emit("tree-node-click", data, node, component);
}
```

父组件：`attrgroup.vue`

```vue
<category @tree-node-click="treenodeclick"></category>
···
<script>
methods:{
    // 感知树节点被点击
    treenodeclick(data, node, component) {
      if (node.level === 3) {
        this.catId = data.catId;
        this.getDataList(); //重新查询
      }
    },
}
</script>
```

Get `/product/attrgroup/list/{catelogId}`

#### QueryWrapper!!!

1. `new QueryWrapper<E>()`：E 查询表格对应实体类
2. `.eq(columu,value);`：查询columu一列等于value值的结果
3. `wrapper.and(()->{})`：拼接查询条件
4. `or()`：或者
5. `like`：双%的like

```java
@Override
    public PageUtils queryPage(Map<String, Object> params, Long cateLogId) {
        if (cateLogId == 0) {
            return queryPage(params);
        } else {
            String key = (String) params.get("key");
            QueryWrapper<AttrGroupEntity> wrapper = new QueryWrapper<AttrGroupEntity>().eq("catelog_id", cateLogId);
            if (!StringUtils.isEmpty(key)) {
                wrapper.and(
                        (obj) -> {
                            obj.eq("attr_group_id", key).or().like("attr_group_name", key);
                        }
                );
            }
            IPage<AttrGroupEntity> page = this.page(new Query<AttrGroupEntity>().getPage(params), wrapper);
            return new PageUtils(page);
        }
    }
```

这个是MyBatis的分页查询

```java
IPage<AttrEntity> page = this.page(
    new Query<AttrEntity>().getPage(params),
    new QueryWrapper<AttrEntity>()
);
return new PageUtils(page);
```

- `Query.getPage(params)`：根据传入的 Map（如 page, limit）生成分页对象。
- `new QueryWrapper<>()`：生成空查询条件。
- 最终作用：分页查询所有 `AttrEntity` 数据（不加任何筛选条件）。

这里的wrapper相当于查询条件

```java
QueryWrapper<Entitiy> wrapper = QueryWrapper<Entitiy>().eq("id",id);
wrapper.add((obj)->{obj.eq("group_id",key).or().like("group_name",key)});
```

这里查询条件"id"=id，然后`add(()->{})`就是拼接查询条件，`.or()`或者，`like()` 等于`%key%`



 这里略过一堆

VO就是value object，就是试图对象，页面传过来的

# 新增商品 

- Controller：处理请求，接受和校验数据
- Service接受controller传来的数据，进行业务处理
- Controller接受Service处理完的数据，封装页面指定的vo

逆向生成VO `https://www.json.cn/json/json2java.html` 生成JavaBean

涉及小数的数据`double`类型改为`BigDecimal`

如果需要在Entity里新增字段，`@TableField(exist = false)` MyBatis-Plus 中的注解，用于表示 **该字段不是数据库表中的列**。

`@JsonInclude(JsonInclude.Include.NON_EMPTY)`  只在序列化时包含 **非空值** 的字段

关于Mybatis

```
<insert id="insertBatch" useGeneratedKeys="true">
```

`useGeneratedKeys="true"`：表示数据库主键是自增的（一般用于获取插入后生成的主键），但这里没有绑定返回主键，其实可以省略。

```
<foreach collection="relations" item="relation" separator=",">
    (#{relation.attrId},#{relation.attrGroupId})
</foreach>
```

`foreach` 是 MyBatis 的动态 SQL 标签，用于 **遍历一个集合（List/Set/数组）并生成重复语句**。

# 仓库管理 

首先要配置负载均衡

这里有一个问题

nacos的dependency默认引入ribbon，但是这个已经停止维护

```xml
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
```

你不手动排除，它就会和 loadbalancer 混用，可能造成：

- 加载两个负载均衡器
- Bean 注入冲突
- 报错：`IllegalStateException: LoadBalancerClientFactory` 或 `No qualifying bean of type`

```xml
<exclusions>
    <exclusion>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-ribbon</artifactId>
    </exclusion>
</exclusions>
```

> **Ribbon 是旧的负载均衡组件，Spring Cloud LoadBalancer 是官方推荐的新组件**，两者都是做客户端负载均衡，但**不兼容，不能共存，会冲突**。

### JSON解释器

```yaml
  jackson:
    date-format: yyyy-MM-dd HH:mm:ss
```

jackson 是默认的JSON解释器

### 开启事务 @Transactional

启动类加上`@EnableTransactionManagement`

`@Transactional` 要放在 **public 方法** 上才会生效（AOP 代理）

如果类内部自调用某个标注了 `@Transactional` 的方法，也不会触发事务（因为没走代理）

默认用的是 Spring AOP 动态代理机制（可切换为 AspectJ）

`type=IdType.INPUT` Mybatis里手动设置主键

| 类型          | 说明                                             |
| ------------- | ------------------------------------------------ |
| `AUTO`        | 数据库自增主键（如 MySQL 的 `AUTO_INCREMENT`）   |
| `NONE`        | 不指定主键策略（默认）                           |
| `INPUT`       | 用户输入（手动设置主键）✅你用的这个              |
| `ASSIGN_ID`   | 使用全局唯一 ID 策略（雪花算法，默认 long 类型） |
| `ASSIGN_UUID` | 使用 UUID 生成主键（字符串类型）                 |

| 实体名                   | 含义                                        |
| ------------------------ | ------------------------------------------- |
| `ProductAttrValueEntity` | 存储产品的属性值，比如颜色=黑色，屏幕=6.5寸 |
| `AttrEntity`             | 属性的定义，比如 “颜色”、“尺寸”、“内存容量” |
| `SpuInfoEntity`          | SPU 基本信息，如名称、品牌、分类            |
| `SkuInfoEntity`          | SKU 基本信息，关联 SPU，具体某个变体        |

###  保存商品 SPU

首先`BeanUtils.copyProperties(source,target)`通过`spuInfoDao`存入SPU表

然后保存SPU 描述 封装在`SpuInfoDescEntity`里，然后这个存在`spuInfoDescDao`里

再保存Spu的图片集 spuImagesService.saveBatch批量保存

保存Spu的规格参数

保存 SPU 的积分信息，远程调用 `coupon` 模块

保存 SPU 关联的所有 SKU 信息： skuinfo，skuimage，skusaleattr，coupon

```rust
start
  |
  v
保存SPU基本信息 -> spuInfoDao.insert(spuInfoEntity)
  |
  v
保存SPU描述图片 -> spuInfoDescDao.insert(spuInfoDescEntity)
  |
  v
保存SPU图片集 -> spuImagesService.saveBatch(spuImages)
  |
  v
保存SPU规格参数 -> productAttrValueService.saveBatch(valueEntities)
  |
  v
远程保存SPU积分信息 -> couponFeignService.saveSpuBounds(spuBoundTo)
  |
  v
遍历所有SKU:
  ├─> 保存SKU基本信息 -> skuInfoService.save(skuInfoEntity)
  |       |
  |       ├─> 保存SKU图片信息 -> skuImagesService.saveBatch(skuImages)
  |       |
  |       ├─> 保存SKU销售属性 -> skuSaleAttrValueService.saveBatch(attrSaleValues)
  |       |
  |       └─> 远程保存SKU优惠信息 -> couponFeignService.saveSkuReduction(skuReductionTo)
  |
  v
end
```

这里调用coupon需要使用openfeign

```
 /**
 * 1.CouponFeignService.saveSpuBounds(spuBoundTo);
 *      1).@RequestBody将spuBoundTo这个对象转为json
 *      2).找到gulimall-coupon这个服务，给coupon/spubounds/save发送请求
 *      将上一步转的json放在请求体位置发送请求
 *      3).对方服务收到请求。收到的是请求体的json数据
 *          @RequestBody SpuBoundsEntity spuBounds: 将请求体的json转为SpuBoundsEntity
 * 只要json数据模型是兼容的，双方服务无需使用同一个to
 * @param spuBoundTo
 * @return
 */
```

这里首先调用Feign，然后找到调用服务的api，然后这里如果模型兼容就ok，比如这里的SpuBoundTo和目标的

```java
@FeignClient(name="coupon")//服务名
public interface CouponFeignService {
    @PostMapping("coupon/spubounds/save")
    R saveSpuBounds(@RequestBody SpuBoundTo spuBoundTo );

    @PostMapping("coupon/skufullreduction/saveInfo")
    R saveSkuReduction(@RequestBody SkuReductionTo skuReductionTo);
}

```

**@JsonIgnoreProperties(ignoreUnknown = true)**：这其实是默认行为，在 Spring Boot 中默认 Jackson 配置会忽略多余字段。

在使用 `@RequestBody`（配合 Jackson 反序列化）时，只有 JSON 中和目标类字段“匹配得上”的字段，才会被赋值。

`@RequestBody`就是将JSON转换成Java对象

# ES 

9300 tcp 废弃, 9200 http

快速地储存、搜索和分析海量数据

维基百科、Stack Overflow、Github 都采用它

底层是开源库 Lucene，Elastic 是 Lucene 的封装，提供了 REST API 的操作接口，开箱即用

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/49e0ca90136abfd99c1686d6935500c4.png)

Index相当于数据库，Type相当于数据表，Document相当于数据

倒排索引机制 Inverted index 略

Docker 安装

elasticsearch：存储和检索数据

kibana：可视化检索数据

```shell
docker pull docker.elastic.co/elasticsearch/elasticsearch:8.17.0
```

chmod权限是 owner，group，others

rwx r4 w2 x1 -0, rwx=7,rw-=6,r-x=5

-d后台运行，-it交互模式

```shell
docker run --name es01 \
-p 9200:9200 -p 9300:9300 \
--restart=always \
-e "discovery.type=single-node" \
-e "xpack.security.enabled=false" \
-e ES_JAVA_OPTS="-Xms512m -Xmx512m" \
-v $PWD/esdata:/usr/share/elasticsearch/data \
-d docker.elastic.co/elasticsearch/elasticsearch:8.17.0
```

如果需要自动启动，docker run里加上 --restart = always

-xms 初始，-xmx最大内存

```shell
(base) ➜  ~ curl http://localhost:9200
{
  "name" : "e3c2ddb8a613",
  "cluster_name" : "docker-cluster",
  "cluster_uuid" : "MnZ2L1SDRlKuIHVPmKPmsw",
  "version" : {
    "number" : "8.17.0",
    "build_flavor" : "default",
    "build_type" : "docker",
    "build_hash" : "2b6a7fed44faa321997703718f07ee0420804b41",
    "build_date" : "2024-12-11T12:08:05.663969764Z",
    "build_snapshot" : false,
    "lucene_version" : "9.12.0",
    "minimum_wire_compatibility_version" : "7.17.0",
    "minimum_index_compatibility_version" : "7.0.0"
  },
  "tagline" : "You Know, for Search"
}
```

```shell
docker pull docker.elastic.co/kibana/kibana:8.17.0

docker run --name kibana \
	--restart=always \
  -e ELASTICSEARCH_HOSTS=http://host.docker.internal:9200 \
  -e SERVER_HOST=0.0.0.0 \
  -e XPACK_SECURITY_ENABLED=false \
  -p 5601:5601 \
  -d docker.elastic.co/kibana/kibana:8.17.0
```

docker**等号两边不能有空格**，它会被当作无效参数

`GET http://localhost:9200/_cat/nodes`

查看所有节点`172.17.0.5 42 85 10 0.48 0.65 0.38 cdfhilmrstw * e3c2ddb8a613`

`GET http://localhost:9200/_cat/health`

查看节点健康情况`1749957310 03:15:10 docker-cluster green 1 1 27 27 0 0 0 0 0 - 100.0%`

`GET http://localhost:9200/_cat/master`

查看主节点`h_kDoe8ARR6GROVFtCUvqQ 172.17.0.5 172.17.0.5 e3c2ddb8a613`

`GET http://localhost:9200/_cat/indices`

查看所有索引 show databases;



# 商品上架 

# 性能压测

# 缓存 

# 检索服务 

# 异步 

# 商品详情 



