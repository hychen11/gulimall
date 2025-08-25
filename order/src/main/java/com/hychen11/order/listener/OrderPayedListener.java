package com.hychen11.order.listener;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.hychen11.order.config.AlipayTemplate;
import com.hychen11.order.service.OrderService;
import com.hychen11.order.vo.PayAsyncVo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ：hychen11
 * @Description:
 * @ClassName: OrderPayedListener
 * @date ：2025/8/25 13:28
 */
@Controller

public class OrderPayedListener {
    @Resource
    private OrderService orderService;
    @Resource
    private AlipayTemplate alipayTemplate;

    @PostMapping("/payed/notify")
    @ResponseBody
    public String handleAlipayed(PayAsyncVo vo, HttpServletRequest request) throws AlipayApiException {
        //只要我们收到了支付宝给我们的异步通知，告诉我们订单支付成功。
        //返回success,支付宝就不再通知了
        //验签
        Map<String, String> params = new HashMap<>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (String name : requestParams.keySet()) {
            String[] values = requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            // 乱码解决，这段代码在出现乱码时使用
            // valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }
        //调用SDK验证签名
        boolean signVerified = AlipaySignature.rsaCheckV1(params, alipayTemplate.getAlipay_public_key(),
                alipayTemplate.getCharset(), alipayTemplate.getSign_type());
        if (signVerified) {
            System.out.println("签名验证成功");
            return orderService.handlePayResult(vo);
        } else {
            return "false";
        }

    }
}
