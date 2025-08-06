package com.hychen11.member.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.hychen11.member.properties.ThreadPoolConfigProperties;

/**
 * @author ：hychen11
 * @Description:
 * @ClassName: ThreadPoolConfig
 * @date ：2025/8/6 23:57
 */
@Configuration
public class ThreadPoolConfig {
    @Autowired
    private ThreadPoolConfigProperties pool;

    @Bean
    public ThreadPoolExecutor threadPoolExecutor() {
        return new ThreadPoolExecutor(pool.getCoreSize(),
                pool.getMaxSize(),
                pool.getKeepAliveTime(), TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(100000), Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy());
    }
}
