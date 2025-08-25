package com.hychen11.seckill.config;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ：hychen11
 * @Description:
 * @ClassName: RabbitMessageConverter
 * @date ：2025/8/18 13:36
 */
@Configuration
public class RabbitMessageConverter {
    /**
     * 配置消息序列化转化器
     *
     * @return
     */
    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
