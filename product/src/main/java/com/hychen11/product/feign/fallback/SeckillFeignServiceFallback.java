package com.hychen11.product.feign.fallback;

import com.hychen11.common.exception.BizCodeEnum;
import com.hychen11.common.utils.R;
import com.hychen11.product.feign.SeckillFeignService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author ：hychen11
 * @Description:
 * @ClassName: SeckillFeignServiceFallback
 * @date ：2025/8/25 21:30
 */
@Component
@Slf4j
public class SeckillFeignServiceFallback implements SeckillFeignService {
    @Override
    public R getSkuSeckillInfo(Long skuId) {
        log.info("熔断方法调用........getSkuSeckillInfo");
        return R.error(BizCodeEnum.TOO_MANY_REQUESTS.getCode(), BizCodeEnum.TOO_MANY_REQUESTS.getMsg());
    }
}
