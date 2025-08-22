package com.hychen11.product.feign;

import com.hychen11.common.to.es.SkuEsModel;
import com.hychen11.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author ：hychen11
 * @Description:
 * @ClassName: SearchFeignService
 * @date ：2025/7/26 15:34
 */
@FeignClient("search")
public interface SearchFeignService {
    @PostMapping("/search/save/product")
    R productStatusUp(@RequestBody List<SkuEsModel> skuEsModels);
}
