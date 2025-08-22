package com.hychen11.common.to.mq;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author ：hychen11
 * @Description:
 * @ClassName: SeckillOrderTo
 * @date ：2025/8/10 17:31
 */
@Data
public class SeckillOrderTo {
    /**
     * 订单号
     */
    private String orderSn;
    /**
     * 活动场次id
     */
    private Long promotionSessionId;
    /**
     * 商品id
     */
    private Long skuId;
    /**
     * 秒杀价
     */
    private BigDecimal seckillPrice;
    /**
     * 购买数量
     */
    private Integer num;
    private Long memberId;
}
