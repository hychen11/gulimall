package com.hychen11.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hychen11.common.utils.PageUtils;
import com.hychen11.product.entity.SkuSaleAttrValueEntity;
import com.hychen11.product.vo.SkuItemSaleAttrsVo;

import java.util.List;
import java.util.Map;

/**
 * sku销售属性&值
 *
 * @author hychen11
 * @email 
 * @date 2025-02-10 15:49:12
 */
public interface SkuSaleAttrValueService extends IService<SkuSaleAttrValueEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<SkuItemSaleAttrsVo> getSaleAttrsBySpuId(Long spuId);
}

