package com.hychen11.product.interceptor;

import com.hychen11.common.constant.AuthServerConstant;
import com.hychen11.common.constant.CartConstant;
import com.hychen11.common.to.MemberTo;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Objects;
import java.util.UUID;

/**
 * @author ：hychen11
 * @Description: 添加用户UserKey标识拦截器
 * @ClassName: UserKeyInterceptor
 * @date ：2025/8/2 13:16
 */
@Component
public class UserKeyInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Cookie[] cookies = request.getCookies();
        if(Objects.nonNull(cookies)){
            for(Cookie cookie : cookies) {
                String cookieName = cookie.getName();
                if (cookieName.equals(CartConstant.TEMP_USER_COOKIE_NAME)) {
                    // 如果存在临时用户cookie，则直接返回
                    return true;
                }
            }
        }else{
            String userKey = UUID.randomUUID().toString().replace("-","");
            Cookie cookie = new Cookie(CartConstant.TEMP_USER_COOKIE_NAME, userKey);
            cookie.setDomain("mall.com");
            cookie.setMaxAge(CartConstant.TEMP_USER_COOKIE_TIMEOUT);
            response.addCookie(cookie);
            return true;
        }
        return true;
    }
}
