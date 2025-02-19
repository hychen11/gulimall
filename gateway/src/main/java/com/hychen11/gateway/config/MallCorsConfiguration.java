package com.hychen11.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

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
