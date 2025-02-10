package com.hychen11.order.dao;

import com.hychen11.order.entity.OrderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单
 * 
 * @author hychen11
 * @email 
 * @date 2025-02-10 16:19:38
 */
@Mapper
public interface OrderDao extends BaseMapper<OrderEntity> {
	
}
