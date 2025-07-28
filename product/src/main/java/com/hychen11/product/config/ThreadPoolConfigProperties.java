package com.hychen11.product.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author ：hychen11
 * @Description:
 * @ClassName: ThreadPoolConfigProperties
 * @date ：2025/7/29 00:01
 */
@ConfigurationProperties(prefix = "thread")
@Data
public class ThreadPoolConfigProperties {
    private Integer coreSize;
    private Integer maxSize;
    private Integer keepAliveTime;
}
