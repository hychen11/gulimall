package com.hychen11.order.web;

import com.alipay.api.AlipayApiException;
import com.hychen11.order.config.AlipayTemplate;
import com.hychen11.order.service.OrderService;
import com.hychen11.order.vo.PayVo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * @author ：hychen11
 * @Description:
 * @ClassName: PayWebController
 * @date ：2025/8/25 12:53
 */
@Controller
public class PayWebController {

    @Resource
    private AlipayTemplate alipayTemplate;
    @Resource
    private OrderService orderService;

    /**
     * 1.将支付页让浏览器展示
     * 2.支付成功以后，要跳转到用户的订单列表页
     *
     * @param orderSn
     * @return
     * @throws AlipayApiException
     */

    @ResponseBody
    @GetMapping(value = "/payOrder", produces = "text/html")
    public String payOrder(@RequestParam String orderSn) throws AlipayApiException {
        PayVo payVo = orderService.getOrderPay(orderSn);
        //返回的是一个页面，将此页面直接交给浏览器
        return alipayTemplate.pay(payVo);
    }
}