package com.hychen11.member.feign;

import com.hychen11.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

/**
 * @author ：hychen11
 * @Description:
 * @ClassName: OrderFeignService
 * @date ：2025/8/10 18:52
 */

@FeignClient("order")
public interface OrderFeignService {
    @PostMapping("/order/order/listWithItem")
    R listWithItem(@RequestBody Map<String, Object> params);
}
