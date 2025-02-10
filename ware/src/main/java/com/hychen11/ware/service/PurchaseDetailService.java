package com.hychen11.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hychen11.common.utils.PageUtils;
import com.hychen11.ware.entity.PurchaseDetailEntity;

import java.util.Map;

/**
 * 
 *
 * @author hychen11
 * @email 
 * @date 2025-02-10 16:18:40
 */
public interface PurchaseDetailService extends IService<PurchaseDetailEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

