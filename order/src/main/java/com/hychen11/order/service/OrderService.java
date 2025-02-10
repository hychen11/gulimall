package com.hychen11.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hychen11.common.utils.PageUtils;
import com.hychen11.order.entity.OrderEntity;

import java.util.Map;

/**
 * 订单
 *
 * @author hychen11
 * @email 
 * @date 2025-02-10 16:19:38
 */
public interface OrderService extends IService<OrderEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

