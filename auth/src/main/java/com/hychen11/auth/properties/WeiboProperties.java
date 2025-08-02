package com.hychen11.auth.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author ：hychen11
 * @Description:
 * @ClassName: WeiboProperties
 * @date ：2025/8/2 13:47
 */
@Data
@ConfigurationProperties(prefix = "oauth2.weibo")
public class WeiboProperties {
    private String clientId;
    private String clientSecret;
    private String registeredRedirectUri;
}
