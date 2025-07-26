package com.hychen11.search.service;

import com.hychen11.common.to.es.SkuEsModel;

import java.io.IOException;
import java.util.List;

/**
 * @author ：hychen11
 * @Description:
 * @ClassName: ProductSaveService
 * @date ：2025/7/22 22:30
 */
public interface ProductSaveService {
    boolean productStatusUp(List<SkuEsModel> skuEsModels) throws IOException;
}
