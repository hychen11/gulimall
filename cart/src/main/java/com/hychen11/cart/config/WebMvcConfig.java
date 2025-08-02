package com.hychen11.cart.config;

import com.hychen11.cart.interceptor.CartInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * @author ：hychen11
 * @Description:
 * @ClassName: WebMvcConfig
 * @date ：2025/7/31 21:55
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Resource
    private CartInterceptor cartInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(cartInterceptor).addPathPatterns("/**");
    }
}
