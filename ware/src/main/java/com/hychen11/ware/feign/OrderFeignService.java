package com.hychen11.ware.feign;

import com.hychen11.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author ：hychen11
 * @Description:
 * @ClassName: OrderFeignService
 * @date ：2025/8/11 01:19
 */
@FeignClient("order")
public interface OrderFeignService {
    @GetMapping("/order/order/getOrder/{orderSn}")
    R getOrderByOrderSn(@PathVariable String orderSn);
}
