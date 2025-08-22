package com.hychen11.order.config;

import com.hychen11.order.interceptor.LoginUserInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author ：hychen11
 * @Description:
 * @ClassName: OrderWebConfiguration
 * @date ：2025/8/12 21:58
 */
@Configuration
public class OrderWebConfiguration implements WebMvcConfigurer {
    @Autowired
    LoginUserInterceptor interceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(interceptor).addPathPatterns("/**");
    }
}
