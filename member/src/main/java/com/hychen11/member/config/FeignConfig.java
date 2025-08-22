package com.hychen11.member.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author ：hychen11
 * @Description:
 * @ClassName: FeignConfig
 * @date ：2025/8/10 18:51
 */
@Configuration
public class FeignConfig {
    /**
     * 解决远程调用请求头丢失问题
     * @return
     */
    @Bean
    public RequestInterceptor requestInterceptor(){
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate requestTemplate) {
                //1.使用RequestContextHolder拿到请求数据
                ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                if(attributes!=null){
                    HttpServletRequest request = attributes.getRequest();
                    //同步请求头数据
                    String cookie = request.getHeader("Cookie");
                    requestTemplate.header("Cookie",cookie);

                }
            }
        };
    }
}
