package com.hychen11.cart;

import com.hychen11.cart.properties.ThreadPoolConfigProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

//nacos注册发现
@EnableDiscoveryClient
//OpenFeign
@EnableFeignClients(basePackages = "com.hychen11.cart.feign")
@EnableRedisHttpSession
@EnableConfigurationProperties(value = {ThreadPoolConfigProperties.class})
@SpringBootApplication
public class CartApplication {
    public static void main(String[] args) {
        SpringApplication.run(CartApplication.class, args);
    }

}
