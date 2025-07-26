package com.hychen11.ware.feign;

import com.hychen11.common.to.SkuInfoTo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author ：hychen11
 * @Description:
 * @ClassName: productFeign
 * @date ：2025/7/20 18:04
 */
@FeignClient(name="product")
public interface ProductFeign {
    @RequestMapping("product/skuinfo/infoBySkuId")
    SkuInfoTo infoBySkuId(@RequestParam Long skuId);
}
