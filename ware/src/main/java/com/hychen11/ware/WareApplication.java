package com.hychen11.ware;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;

@EnableDiscoveryClient
@SpringBootApplication
@MapperScan("com.hychen11.ware.dao")
@EnableFeignClients
@EnableTransactionManagement
@EnableRabbit
public class WareApplication {

    public static void main(String[] args) {
        SpringApplication.run(WareApplication.class, args);
    }

}
