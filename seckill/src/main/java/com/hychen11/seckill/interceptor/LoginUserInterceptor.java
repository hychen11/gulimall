package com.hychen11.seckill.interceptor;

import com.hychen11.common.constant.AuthServerConstant;
import com.hychen11.common.to.MemberTo;
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
 * @date ：2025/8/23 23:06
 */
@Configuration
public class LoginUserInterceptor implements HandlerInterceptor {
    public static ThreadLocal<MemberTo> loginUser = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        boolean match = antPathMatcher.match("/kill", uri);
        if (match) {
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
        return true;
    }
}
