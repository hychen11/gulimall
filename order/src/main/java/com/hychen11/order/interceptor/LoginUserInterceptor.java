package com.hychen11.order.interceptor;

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
 * @date ：2025/8/7 00:02
 */
@Configuration
public class LoginUserInterceptor implements HandlerInterceptor {
    public static ThreadLocal<MemberTo> loginUser = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //放行 /order/order/getOrder/{orderSn}请求
        //TODO: 看视频P296讲解
        String uri = request.getRequestURI();
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        boolean match = antPathMatcher.match("/order/order/getOrder/**", uri);
        boolean match1 = antPathMatcher.match("/payed/**", uri);
        if (match || match1) {
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
