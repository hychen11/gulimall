package com.hychen11.seckill.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ：hychen11
 * @Description:
 * @ClassName: RedissonConfig
 * @date ：2025/8/23 23:01
 */
@Configuration
public class RedissonConfig {
    @Bean(destroyMethod = "shutdown")
    public RedissonClient redissonClient() {
        //1.创建配置
        Config config = new Config();
        config.useSingleServer().setAddress("redis://localhost:6380");
        //2.根据Config创建出RedissonClient实例
        return Redisson.create(config);
    }
}