package com.hychen11.cart.interceptor;

import com.alibaba.nacos.common.utils.StringUtils;
import com.hychen11.cart.to.UserInfoTo;
import com.hychen11.common.constant.CartConstant;
import com.hychen11.common.to.MemberTo;
import com.hychen11.common.constant.AuthServerConstant;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Objects;
import java.util.UUID;

/**
 * @author ：hychen11
 * @Description: judge user login status
 * @ClassName: CartInterceptor
 * @date ：2025/7/31 21:56
 */
@Component
public class CartInterceptor implements HandlerInterceptor {
    //ThreadLocal 存用户信息
    public static ThreadLocal<UserInfoTo> threadLocal = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        MemberTo member = (MemberTo) session.getAttribute(AuthServerConstant.LOGIN_USER);
        UserInfoTo userInfo = new UserInfoTo();
        if (member == null) {
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    String cookieName = cookie.getName();
                    if (cookieName.equals(CartConstant.TEMP_USER_COOKIE_NAME)) {
                        userInfo.setUserKey(cookie.getValue());
                    }
                }
                if (userInfo.getUserKey() == null) {
                    String userKey = UUID.randomUUID().toString().replace("_", "");
                    userInfo.setUserKey(userKey);
                    Cookie cookie1 = new Cookie(CartConstant.TEMP_USER_COOKIE_NAME, userKey);
                    cookie1.setDomain("mall.com");
                    cookie1.setMaxAge(CartConstant.TEMP_USER_COOKIE_TIMEOUT);
                    response.addCookie(cookie1);
                }
            }
        } else {
            userInfo.setUserId(member.getId());
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    String cookieName = cookie.getName();
                    if (cookieName.equals(CartConstant.TEMP_USER_COOKIE_NAME)) {
                        userInfo.setUserKey(cookie.getValue());
                    }
                }
            }
        }
        threadLocal.set(userInfo);
        return true;
    }
}
