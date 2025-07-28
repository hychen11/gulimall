package com.hychen11.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hychen11.common.utils.PageUtils;
import com.hychen11.product.entity.BrandEntity;

import java.util.List;
import java.util.Map;

/**
 * 品牌
 *
 * @author hychen11
 * @email 
 * @date 2025-02-10 15:49:12
 */
public interface BrandService extends IService<BrandEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void updateCascade(BrandEntity brand);

    List<BrandEntity> getBrandIds(List<Long> brandIds);
}

