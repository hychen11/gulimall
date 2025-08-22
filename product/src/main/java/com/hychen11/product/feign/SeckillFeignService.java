package com.hychen11.product.feign;

import com.hychen11.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author ：hychen11
 * @Description:
 * @ClassName: SeckillFeignService
 * @date ：2025/8/10 19:29
 */
@FeignClient("seckill")
public interface SeckillFeignService {
    @GetMapping("/sku/seckill/{skuId}")
    R getSkuSeckillInfo(@PathVariable Long skuId);
}
