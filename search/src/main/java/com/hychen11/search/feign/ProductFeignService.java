package com.hychen11.search.feign;

import com.hychen11.common.to.AttrRespTo;
import com.hychen11.common.to.BrandTo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author ：hychen11
 * @Description:
 * @ClassName: ProductFeignService
 * @date ：2025/7/29 00:49
 */
@FeignClient(name = "search")
public interface ProductFeignService {
    @GetMapping("/product/attr/feign/info/{attrId}")
    AttrRespTo feign_info(@PathVariable("attrId") Long attrId);

    @GetMapping("/product/brand/feign/infos")
    List<BrandTo> infos(@RequestParam List<Long> brandIds);
}
