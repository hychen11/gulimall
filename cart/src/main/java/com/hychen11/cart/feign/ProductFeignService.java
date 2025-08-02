package com.hychen11.cart.feign;

import com.hychen11.common.to.SkuInfoTo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("product")
public interface ProductFeignService {
    @RequestMapping("/product/skuinfo/infoBySkuId")
    SkuInfoTo infoBySkuId(@RequestParam("skuId") Long skuId);

    @GetMapping("/product/skusaleattrvalue/stringList/{skuId}")
    List<String> getSkuSaleAttrValues(@PathVariable Long skuId);
}
