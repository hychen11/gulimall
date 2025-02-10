package com.hychen11.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hychen11.common.utils.PageUtils;
import com.hychen11.product.entity.AttrEntity;

import java.util.Map;

/**
 * 商品属性
 *
 * @author hychen11
 * @email 
 * @date 2025-02-10 15:49:12
 */
public interface AttrService extends IService<AttrEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

