package com.hychen11.order.web;

import com.hychen11.common.exception.ErrorCodeEnum;
import com.hychen11.common.exception.NoStockException;
import com.hychen11.order.vo.OrderSubmitVo;
import com.hychen11.order.vo.SubmitOrderResVo;
import org.springframework.ui.Model;
import com.hychen11.order.service.OrderService;
import com.hychen11.order.vo.OrderConfirmVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import java.util.concurrent.ExecutionException;

/**
 * @author ：hychen11
 * @Description:
 * @ClassName: OrderWebController
 * @date ：2025/8/12 21:51
 */
@Controller
@Slf4j
public class OrderWebController {
    @Autowired
    private OrderService orderService;

    @GetMapping("/toTrade")
    public String toTrade(Model model) throws ExecutionException, InterruptedException {
        OrderConfirmVo confirmVo = orderService.confirmOrder();
        model.addAttribute("OrderConfirmData", confirmVo);
        return "confirm";
    }

    /**
     * 提交订单功能
     *
     * @param orderSubmitVo
     * @return
     */
    @PostMapping("/submitOrder")
    public String submitOrder(OrderSubmitVo orderSubmitVo, Model model, RedirectAttributes redirectAttributes) {
        // 1、创建订单、验令牌、验价格、验库存
        try {
            SubmitOrderResVo submitOrderResVo = orderService.submitOrder(orderSubmitVo);
            int res = submitOrderResVo.getCode();
            if (res == 0) {
                model.addAttribute("submitOrderRes", submitOrderResVo);
                return "pay";
            } else {
                String msg = "fail to submit order";
                switch (res) {
                    case 1:
                        msg += "令牌校验失败";
                        break;
                    case 2:
                        msg += "锁失败";
                        break;
                    case 3:
                        msg += "金额对比失败";
                        break;
                    case 4:
                        msg += "验证令牌为空";
                        break;
                }
                redirectAttributes.addFlashAttribute("msg", msg);
                return "redirect:http://order.mall.com/toTrade";
            }
        } catch (Exception e) {
            if (e instanceof NoStockException) {
                String msg = e.getMessage();
                redirectAttributes.addFlashAttribute("msg", msg);
            } else {
                log.error(e.getMessage());
                redirectAttributes.addFlashAttribute("msg", ErrorCodeEnum.UNKNOW_EXCEPTION.getMessage());
            }
            return "redirect:http://order.mall.com/toTrade";
        }
    }
}
