package com.hychen11.product.feign;

import com.hychen11.common.utils.R;
import com.hychen11.product.feign.fallback.SeckillFeignServiceFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author ：hychen11
 * @Description:
 * @ClassName: SeckillFeignService
 * @date ：2025/8/10 19:29
 */
//@FeignClient("seckill")
@FeignClient(value = "mall-seckill", fallback = SeckillFeignServiceFallback.class)
public interface SeckillFeignService {
    @GetMapping("/sku/seckill/{skuId}")
    R getSkuSeckillInfo(@PathVariable("skuId") Long skuId);
}
