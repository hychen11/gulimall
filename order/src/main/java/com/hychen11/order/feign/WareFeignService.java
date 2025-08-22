package com.hychen11.order.feign;

import com.hychen11.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import com.hychen11.order.vo.SkuHasStockVo;
import com.hychen11.order.vo.WareSkuLockVo;

import java.util.List;

/**
 * @author ：hychen11
 * @Description:
 * @ClassName: WareFeignService
 * @date ：2025/8/12 22:28
 */
@FeignClient("ware")
public interface WareFeignService {
    @PostMapping("/ware/waresku/hasStock")
    List<SkuHasStockVo> getSkusHasStock(@RequestBody List<Long> skuIds);

    @GetMapping("/ware/wareinfo/getFare")
    R getFare(@RequestParam("addrId") Long attrId);

    @PostMapping("/ware/waresku/lock/order")
    R orderLockStock(@RequestBody WareSkuLockVo vo);
}
