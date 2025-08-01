package com.hychen11.product;

import com.hychen11.product.config.ThreadPoolConfigProperties;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@EnableDiscoveryClient
@SpringBootApplication
@MapperScan("com.hychen11.product.dao")
@EnableFeignClients(basePackages = "com.hychen11.product.feign")
@EnableTransactionManagement
@EnableCaching
@EnableRedisHttpSession
@EnableConfigurationProperties(value = {ThreadPoolConfigProperties.class})
public class ProductApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProductApplication.class, args);
    }
}


