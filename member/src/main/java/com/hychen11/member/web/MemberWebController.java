package com.hychen11.member.web;

import com.hychen11.common.to.MemberTo;
import com.hychen11.common.utils.R;
import com.hychen11.member.feign.OrderFeignService;
import com.hychen11.member.interceptor.LoginUserInterceptor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ：hychen11
 * @Description:
 * @ClassName: MemberWebController
 * @date ：2025/8/10 19:25
 */
@Controller
public class MemberWebController {
    @Resource
    private OrderFeignService orderFeignService;

    @GetMapping("/memberOrder.html")
    public String memberOrderPage(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum, Model model, HttpServletRequest request) {
        //获取到支付宝给我们传过来的所有请求数据
        //验证签名，如果正确去修改

        //查出当前登陆的用户的所用订单列表数据
        MemberTo member = LoginUserInterceptor.loginUser.get();
        Long id = member.getId();
        Map<String, Object> params = new HashMap<>();
        params.put("page", pageNum.toString());
        R r = orderFeignService.listWithItem(params);
        model.addAttribute("orders", r);
        return "orderList";
    }
}
