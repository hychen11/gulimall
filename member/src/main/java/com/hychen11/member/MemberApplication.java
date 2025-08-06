package com.hychen11.member;

import com.hychen11.member.properties.ThreadPoolConfigProperties;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@EnableConfigurationProperties(ThreadPoolConfigProperties.class)
@EnableRedisHttpSession

@EnableFeignClients(basePackages = "com.hychen11.member.feign")
@EnableDiscoveryClient
@SpringBootApplication
@MapperScan("com.hychen11.member.dao")
public class MemberApplication {

    public static void main(String[] args) {
        SpringApplication.run(MemberApplication.class, args);
    }

}
