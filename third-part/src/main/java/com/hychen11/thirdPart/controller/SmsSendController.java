package com.hychen11.thirdPart.controller;

import com.hychen11.common.utils.R;
import com.hychen11.thirdPart.component.SmsComponent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author ：hychen11
 * @Description:
 * @ClassName: SmsSendController
 * @date ：2025/8/2 16:48
 */
@RestController
@RequestMapping("/sms")
public class SmsSendController {
    @Resource
    private SmsComponent sms;

    @GetMapping("/sendCode")
    public R sendSms(@RequestParam("phone") String phone, @RequestParam("code") String code) {
        sms.sendSms(phone, code);
        return R.ok();
    }
}
