package com.hychen11.auth.controller;

import com.alibaba.fastjson.TypeReference;
import com.hychen11.auth.feign.MemberFeign;
import com.hychen11.auth.feign.ThirdPartyFeign;
import com.hychen11.auth.vo.RegisterVo;
import com.hychen11.auth.vo.UserLoginVo;
import com.hychen11.common.constant.AuthServerConstant;
import com.hychen11.common.exception.ErrorCodeEnum;
import com.hychen11.common.to.MemberRegisterTo;
import com.hychen11.common.to.MemberTo;
import com.hychen11.common.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author ：hychen11
 * @Description:
 * @ClassName: LoginController
 * @date ：2025/8/2 13:45
 */
@Slf4j
@Controller
public class LoginController {
    @Autowired
    private ThirdPartyFeign thirdPartyFeign;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private MemberFeign memberFeign;

    private static final Long RepeatTime = 6000L;

    @GetMapping("/login.html")
    public String LoginPage(HttpSession session) {
        Object loginUser = session.getAttribute(AuthServerConstant.LOGIN_USER);
        if (Objects.isNull(loginUser)) {
            return "login";
        }
        return "redirect:http://mall.com";
    }

    /**
     * 发送验证码
     */
    @ResponseBody
    @GetMapping("/sms/sendCode")
    public R sendCode(@RequestParam("phone") String phone) {
        String redisCode = redisTemplate.opsForValue().get(AuthServerConstant.SMS_CODE_CACHE_PREFIX + phone);
        if (!StringUtils.isEmpty(redisCode)) {
            long l = Long.parseLong(redisCode.split("_")[1]);
            if (System.currentTimeMillis() - l < RepeatTime) {
                //60 s内不能重复发送验证码
                return R.error(ErrorCodeEnum.SMS_CODE_EXCEPTION.getCode(), ErrorCodeEnum.SMS_CODE_EXCEPTION.getMessage());
            }
        }
        //接口防刷，验证码校验
        Random random = new Random();
        int i = 10000 + random.nextInt(90000);
        String code = String.valueOf(i);
        String codeTime = code + "_" + System.currentTimeMillis();
        redisTemplate.opsForValue().set(AuthServerConstant.SMS_CODE_CACHE_PREFIX + phone, codeTime, 5, TimeUnit.MINUTES);
        log.info("code:{}", code);
        thirdPartyFeign.sendSms(phone, code);
        return R.ok();
    }

    //重定向携带数据，利用session原理，将数据放在session中，只要跳到下一个页面取出这个数据以后，session里面数据就会删掉
    //TODO 存在分布式session的问题
    @PostMapping("/register")
    public String register(@Valid RegisterVo registerVo, BindingResult result,
                           RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            //校验出现错误
            Map<String, String> error = result.getFieldErrors().stream().collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
            redirectAttributes.addFlashAttribute("errors", error);
            return "redirect:http://auth.mall.com/reg.html";
        }
        String code = registerVo.getCode();
        String phone = registerVo.getPhone();
        String redisValue = redisTemplate.opsForValue().get(AuthServerConstant.SMS_CODE_CACHE_PREFIX + phone);
        if (!StringUtils.isEmpty(redisValue)) {
            String redisCode = redisValue.split("-")[0];
            if (redisCode.equals(code)) {
                MemberRegisterTo memberRegisterTo = new MemberRegisterTo();
                BeanUtils.copyProperties(registerVo, memberRegisterTo);
                R r = memberFeign.register(memberRegisterTo);
                //registered successfully
                if (r.getCode() == 0) {
                    redisTemplate.delete(AuthServerConstant.SMS_CODE_CACHE_PREFIX + phone);
                    return "redirect:http://auth.mall.com/login.html";
                } else {
                    Map<String, String> errors = new HashMap<>();
                    errors.put("msg", r.getMessage());
                    redirectAttributes.addFlashAttribute("errors", errors);
                    return "redirect:http://auth.mall.com/reg.html";
                }
            } else {
                //smscode error
                Map<String, String> errors = new HashMap<>();
                errors.put("code", "smscode error");
                redirectAttributes.addFlashAttribute("errors", errors);
                return "redirect:http://auth.mall.com/reg.html";
            }
        } else {
            //没点击发送验证码
            Map<String, String> errors = new HashMap<>();
            errors.put("code", "empty smscode error");
            redirectAttributes.addFlashAttribute("errors", errors);
            return "redirect:http://auth.mall.com/reg.html";
        }
    }

    /**
     * 登录功能
     *
     * @param user
     * @param redirectAttributes
     * @return
     */
    @PostMapping("/login")
    public String login(UserLoginVo user, RedirectAttributes redirectAttributes, HttpSession session) {
        R r = memberFeign.login(user);
        if (r.getCode() == 0) {
            MemberTo memberTo = r.getData2("data", new TypeReference<MemberTo>() {
            });
            session.setAttribute(AuthServerConstant.LOGIN_USER, memberTo);
            return "redirect:http://mall.com";
        }
        Map<String, String> errors = new HashMap<>();
        errors.put("msg", r.getMessage());
        redirectAttributes.addFlashAttribute("errors", errors);
        return "redirect:http://auth.mall.com/login.html";
    }
}
