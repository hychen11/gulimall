package com.hychen11.seckill.to;

import com.hychen11.seckill.vo.SkuInfoVo;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author ：hychen11
 * @Description:
 * @ClassName: SeckillSkuRedisTo
 * @date ：2025/8/23 23:08
 */
@Data
public class SeckillSkuRedisTo {
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
    //sku的详细信息
    private SkuInfoVo skuInfoVo;
    //当前秒杀开始和结束时间
    private Long startTime;
    private Long endTime;
    //秒杀随机码
    private String randomCode;
}
