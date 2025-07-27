package com.hychen11.product.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

/**
 * @author ：hychen11
 * @Description:
 * @ClassName: CacheMessageListener
 * @date ：2025/7/27 21:01
 */
@Service
@RabbitListener(queues = "cache.clear.queue")
@Slf4j
public class CacheMessageListener {
    @Autowired
    private RedisTemplate redisTemplate;
    public void handle(String key){
        redisTemplate.delete(key);
    }
}
