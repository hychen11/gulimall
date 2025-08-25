package com.hychen11.seckill.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author ：hychen11
 * @Description:
 * @ClassName: SeckillSkuRelationVo
 * @date ：2025/8/23 23:12
 */
@Data
public class SeckillSkuRelationVo {
    /**
     * id
     */
    private Long id;
    /**
     * 活动id
     */
    private Long promotionId;
    /**
     * 活动场次id
     */
    private Long promotionSessionId;
    /**
     * 商品id
     */
    private Long skuId;
    /**
     * 秒杀价格
     */
    private BigDecimal seckillPrice;
    /**
     * 秒杀总量
     */
    private BigDecimal seckillCount;
    /**
     * 每人限购数量
     */
    private BigDecimal seckillLimit;
    /**
     * 排序
     */
    private Integer seckillSort;
}
