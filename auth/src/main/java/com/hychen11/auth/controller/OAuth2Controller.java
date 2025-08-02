package com.hychen11.auth.controller;

import com.alibaba.fastjson2.JSON;
import com.hychen11.auth.feign.MemberFeign;
import com.hychen11.auth.properties.WeiboProperties;
import com.hychen11.auth.vo.WeiboUserVo;
import com.hychen11.common.constant.AuthServerConstant;
import com.hychen11.common.to.MemberTo;
import com.hychen11.common.utils.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ：hychen11
 * @Description:
 * @ClassName: OAuth2Controller
 * @date ：2025/8/2 13:46
 */
@Slf4j
@Controller
@RequestMapping("/oauth2.0")
public class OAuth2Controller {
    @Autowired
    private WeiboProperties weiboProperties;
    @Autowired
    private MemberFeign memberFeign;

    @GetMapping("/weibo/success")
    public String weibo(@RequestParam("code") String code, HttpSession session) {
        //1.根据code,换取AccessToken
        //https://api.weibo.com/oauth2/access_token?client_id=YOUR_CLIENT_ID&client_secret=YOUR_CLIENT_SECRET&grant_type=authorization_code&redirect_uri=YOUR_REGISTERED_REDIRECT_URI&code=CODE
        Map<String, String> map = new HashMap<>();
        map.put("client_id", weiboProperties.getClientId());
        map.put("client_secret", weiboProperties.getClientSecret());
        map.put("grant_type", "authorization_code");
        map.put("redirect_uri", weiboProperties.getRegisteredRedirectUri());
        map.put("code", code);
        try {
            HttpResponse response = HttpUtils.doPost("https://api.weibo.com", "/oauth2/access_token", "post", new HashMap<String, String>(), null, map);
            //2.处理AccessToken
            if (response.getStatusLine().getStatusCode() == 200) {
                //获取到了AccessToken
                WeiboUserVo weiboUser = JSON.parseObject(EntityUtils.toString(response.getEntity()), WeiboUserVo.class);
                String accessToken = weiboUser.getAccess_token();
                //获取用户UID
                //https://api.weibo.com/2/account/get_uid.json
                HashMap<String, String> map1 = new HashMap<>();
                map1.put("access_token", accessToken);
                HttpResponse response1 = HttpUtils.doGet("https://api.weibo.com", "2/account/get_uid.json", "get", new HashMap<String, String>(), map1);
                if (response.getStatusLine().getStatusCode() == 200) {
                    String uid = JSON.parseObject(EntityUtils.toString(response1.getEntity()), String.class);
                    weiboUser.setUid(uid);
                    //判断该用户如果第一次进我们网站，自动注册(为当前社交用户生成一个用户信息账号,以后找个社交账号就指定对应的会员信息账号)
                    MemberTo memberTo = memberFeign.oauthLogin(weiboUser);
                    log.info(memberTo.toString());
                    //2.登录成功调回首页
                    session.setAttribute(AuthServerConstant.LOGIN_USER, memberTo);
                    return "redirect:http://mall.com";
                }
            }
            return "redirect:http://auth.mall.com/login.html";
        } catch (Exception e) {
            log.error("微博社交登录出错:{}", e.getMessage());
            return "redirect:http://auth.mall.com/login.html";
        }
    }
}