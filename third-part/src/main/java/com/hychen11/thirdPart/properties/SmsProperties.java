package com.hychen11.thirdPart.properties;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author ：hychen11
 * @Description:
 * @ClassName: SmsProperties
 * @date ：2025/8/2 16:49
 */
@ConfigurationProperties(prefix = "spring.alicloud.sms")
@Data
public class SmsProperties {
    private String host;

    private String path;

    private String appcode;

    private String smsSignId;

    private String templateId;
}
