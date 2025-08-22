package com.hychen11.order.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.hychen11.order.vo.SpuInfoVo;

/**
 * @author ：hychen11
 * @Description:
 * @ClassName: ProductFeignService
 * @date ：2025/8/12 22:31
 */
@FeignClient("product")
public interface ProductFeignService {
    @GetMapping("/product/spuinfo/getSpuInfoBySkuId/{skuId}")
    SpuInfoVo getSpuInfoBySkuId(@PathVariable Long skuId);

    @GetMapping("/product/brand/getBrandNameById/{brandId}")
    String getBrandNameById(@PathVariable Long brandId);
}
