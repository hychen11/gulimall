package com.hychen11.product.feign;

import com.hychen11.common.to.SkuReductionTo;
import com.hychen11.common.to.SpuBoundTo;
import com.hychen11.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author ：hychen11
 * @Description:
 * @ClassName: CouponFeignService
 * @date ：2025/7/20 14:31
 */
@FeignClient("coupon")
public interface CouponFeignService {

    @PostMapping("coupon/spubounds/save")
    R saveSpuBounds(@RequestBody SpuBoundTo spuBoundTo );

    @PostMapping("coupon/skufullreduction/saveInfo")
    R saveSkuReduction(@RequestBody SkuReductionTo skuReductionTo);
}
