package com.hychen11.order.properties;

/**
 * @author ：hychen11
 * @Description:
 * @ClassName: ThreadPoolConfigProperties
 * @date ：2025/8/7 00:11
 */

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "thread")
@Data
public class ThreadPoolConfigProperties {
    private Integer coreSize;
    private Integer maxSize;
    private Integer keepAliveTime;
}
