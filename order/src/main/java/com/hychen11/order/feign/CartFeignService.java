package com.hychen11.order.feign;

import com.hychen11.order.vo.OrderItemVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * @author ：hychen11
 * @Description:
 * @ClassName: CartFeignService
 * @date ：2025/8/12 22:28
 */
@FeignClient("cart")
public interface CartFeignService {
    @GetMapping("/currentUserCartItems")
    List<OrderItemVo> getCurrentUserCartItems();
}
