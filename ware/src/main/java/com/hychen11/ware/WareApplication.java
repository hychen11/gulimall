package com.hychen11.ware;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
@MapperScan("com.hychen11.ware.dao")
public class WareApplication {

    public static void main(String[] args) {
        SpringApplication.run(WareApplication.class, args);
    }

}
