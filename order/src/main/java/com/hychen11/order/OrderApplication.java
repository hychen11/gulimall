package com.hychen11.order;

import com.hychen11.order.properties.ThreadPoolConfigProperties;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableRedisHttpSession
@EnableTransactionManagement
@EnableConfigurationProperties(value = {ThreadPoolConfigProperties.class})
@EnableRabbit

@EnableDiscoveryClient
@SpringBootApplication
@MapperScan("com.hychen11.order.dao")
@EnableFeignClients

public class OrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
    }

}
