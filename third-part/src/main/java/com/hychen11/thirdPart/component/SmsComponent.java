package com.hychen11.thirdPart.component;

import com.hychen11.common.utils.HttpUtils;
import com.hychen11.thirdPart.properties.SmsProperties;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ：hychen11
 * @Description:
 * @ClassName: SmsComponent
 * @date ：2025/8/2 16:49
 */
@Component
public class SmsComponent {
    @Autowired
    private SmsProperties sms;


    public void sendSms(String phone, String code) {
        String method = "POST";
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "APPCODE " + sms.getAppcode());
        Map<String, String> querys = new HashMap<>();
        querys.put("mobile", phone);
        querys.put("param", "**code**:" + code + ",**minute**:5");

        querys.put("smsSignId", sms.getSmsSignId());
        querys.put("templateId", sms.getTemplateId());
        Map<String, String> bodys = new HashMap<>();
        try {
            HttpResponse response = HttpUtils.doPost(sms.getHost(), sms.getPath(), method, headers, querys, bodys);
            System.out.println(response.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
