package com.hychen11.search.controller;

import com.hychen11.common.exception.ErrorCodeEnum;
import com.hychen11.common.to.es.SkuEsModel;
import com.hychen11.common.utils.R;
import com.hychen11.search.service.ProductSaveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author ：hychen11
 * @Description:
 * @ClassName: ElasticSaveController
 * @date ：2025/7/22 21:28
 */
@Slf4j
@RestController
@RequestMapping("/search/save")
public class ElasticSaveController {
    @Autowired
    private ProductSaveService productSaveService;

    @PostMapping("product")
    public R productStatusUp(@RequestBody List<SkuEsModel> skuEsModels) {
        boolean b = false;
        try {
            b = productSaveService.productStatusUp(skuEsModels);
        } catch (Exception e) {
            log.error("ElasticSaveController#productStatusUp:{}", e.getMessage());
            return R.error(ErrorCodeEnum.PRODUCT_UP_EXCEPTION.getCode(), ErrorCodeEnum.PRODUCT_UP_EXCEPTION.getMessage());
        }
        if (!b) {
            return R.ok();
        } else {
            return R.error(ErrorCodeEnum.PRODUCT_UP_EXCEPTION.getCode(), ErrorCodeEnum.PRODUCT_UP_EXCEPTION.getMessage());
        }
    }
}
