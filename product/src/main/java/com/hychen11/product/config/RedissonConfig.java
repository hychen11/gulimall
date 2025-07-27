package com.hychen11.product.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.redisson.config.Config;

/**
 * @author ：hychen11
 * @Description:
 * @ClassName: RedissonConfig
 * @date ：2025/7/26 19:53
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
