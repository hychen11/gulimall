package com.hychen11.seckill.feign;

import com.hychen11.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author ：hychen11
 * @Description:
 * @ClassName: CouponFeignService
 * @date ：2025/8/23 23:08
 */
@FeignClient("coupon")
public interface CouponFeignService {
    @RequestMapping("/coupon/seckillsession/latest3DaySession")
    R getLatest3DaySession();
}
