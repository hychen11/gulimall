package com.hychen11.seckill.feign;

import com.hychen11.seckill.vo.SkuInfoVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author ：hychen11
 * @Description:
 * @ClassName: ProductFeignService
 * @date ：2025/8/23 23:08
 */
@FeignClient("product")
public interface ProductFeignService {
    @RequestMapping("/product/skuinfo/infoBySkuId")
    SkuInfoVo infoBySkuId(@RequestParam Long skuId);
}
