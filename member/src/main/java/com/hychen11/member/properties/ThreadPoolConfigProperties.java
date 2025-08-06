package com.hychen11.member.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author ：hychen11
 * @Description:
 * @ClassName: ThreadPoolConfigProperties
 * @date ：2025/8/6 23:53
 */
@ConfigurationProperties(prefix = "thread")
@Data
public class ThreadPoolConfigProperties {
    private Integer coreSize;
    private Integer maxSize;
    private Integer keepAliveTime;
}
