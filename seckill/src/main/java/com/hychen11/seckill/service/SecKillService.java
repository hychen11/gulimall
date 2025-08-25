package com.hychen11.seckill.service;

import com.hychen11.seckill.to.SeckillSkuRedisTo;

import java.util.List;

/**
 * @author ：hychen11
 * @Description:
 * @ClassName: SecKillService
 * @date ：2025/8/23 23:07
 */
public interface SecKillService {
    void uploadSecKillLatest3Days();

    List<SeckillSkuRedisTo> getCurrentTimeSeckillSkus();

    SeckillSkuRedisTo getSkuSeckillInfo(Long skuId);

    String kill(String killId, String key, Integer num);
}
