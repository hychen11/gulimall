package com.hychen11.order.vo;

import lombok.Data;

/**
 * @author ：hychen11
 * @Description:
 * @ClassName: PayVo
 * @date ：2025/8/14 18:59
 */
@Data
public class PayVo {
    private String out_trade_no; // 商户订单号 必填
    private String subject; // 订单名称 必填
    private String total_amount;  // 付款金额 必填
    private String body; // 商品描述 可空
}
