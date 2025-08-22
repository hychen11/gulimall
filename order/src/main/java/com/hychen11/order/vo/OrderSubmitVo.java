package com.hychen11.order.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author ：hychen11
 * @Description:
 * @ClassName: OrderSubmitVo
 * @date ：2025/8/14 18:58
 */
@Data
public class OrderSubmitVo {
    private Long addrId; //收货地址id
    private Integer payType; //支付方式
    //无需提交需要购买的商品，去购物车服务再获取一遍
    //优惠发票等
    private String orderToken; //防重令牌
    private BigDecimal payPrice; //应付总额
    //用户信息在session中，不用提交
    private String note; //订单备注
}
