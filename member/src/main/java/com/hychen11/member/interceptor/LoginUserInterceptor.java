package com.hychen11.member.interceptor;

import com.hychen11.common.constant.AuthServerConstant;
import com.hychen11.common.to.MemberTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author ：hychen11
 * @Description:
 * @ClassName: LoginUserInterceptor
 * @date ：2025/8/10 18:50
 */
@Configuration
public class LoginUserInterceptor implements HandlerInterceptor {
    public static ThreadLocal<MemberTo> loginUser = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();
        boolean match = new AntPathMatcher().match("/member/**", uri);
        if (match) {
            return true;
        }
        HttpSession session = request.getSession();
        MemberTo member = (MemberTo) session.getAttribute(AuthServerConstant.LOGIN_USER);
        if (member != null) {
            loginUser.set(member);
            return true;
        } else {
            request.getSession().setAttribute("msg", "请先进行登录");
            response.sendRedirect("http://auth.mall.com/login.html");
            return false;
        }

    }
}
