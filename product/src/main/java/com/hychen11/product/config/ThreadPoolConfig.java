package com.hychen11.product.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author ：hychen11
 * @Description:
 * @ClassName: ThreadPoolConfig
 * @date ：2025/7/29 00:00
 */
@Configuration
public class ThreadPoolConfig {
    @Resource
    private ThreadPoolConfigProperties pool;
    @Bean
    public ThreadPoolExecutor threadPoolExecutor() {
        return new ThreadPoolExecutor(
                pool.getCoreSize(),
                pool.getMaxSize(),
                pool.getKeepAliveTime(),
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(100000),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy()
        );
    }
}
