package com.hychen11.product.feign;

import com.hychen11.common.to.SkuHasStockTo;
import com.hychen11.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author ：hychen11
 * @Description:
 * @ClassName: WareFeignService
 * @date ：2025/7/26 15:51
 */
@FeignClient(name = "ware")
public interface WareFeignService {
    @RequestMapping("ware/wareinfo/list")
    R list(@RequestParam Map<String, Object> params);

    @PostMapping("ware/waresku/hasStock")
    List<SkuHasStockTo> getSkusHasStock(@RequestBody List<Long> skuIds);

    @GetMapping("ware/waresku/{skuId}/hasStock")
    boolean hasStockBySkuId(@PathVariable Long skuId);
}
