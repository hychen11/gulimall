package com.hychen11.auth.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author ：hychen11
 * @Description:
 * @ClassName: ThirdPartyFeign
 * @date ：2025/8/2 13:46
 */
@FeignClient("third-party")
public interface ThirdPartyFeign {

    @GetMapping("/sms/sendCode")
    void sendSms(@RequestParam("phone") String phone, @RequestParam("code") String code);
}
