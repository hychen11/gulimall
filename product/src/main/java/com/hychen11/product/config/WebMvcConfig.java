package com.hychen11.product.config;

import com.hychen11.product.interceptor.UserKeyInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * @author ：hychen11
 * @Description:
 * @ClassName: WebMvcConfig
 * @date ：2025/8/2 13:16
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Resource
    private UserKeyInterceptor userKeyInterceptor;

    /**
     * 拦截器注册
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userKeyInterceptor).addPathPatterns("/**");
    }
}
