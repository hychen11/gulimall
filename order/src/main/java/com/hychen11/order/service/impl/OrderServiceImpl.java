package com.hychen11.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hychen11.common.to.MemberTo;
import com.hychen11.order.entity.OrderItemEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hychen11.common.utils.PageUtils;
import com.hychen11.common.utils.Query;

import com.hychen11.order.dao.OrderDao;
import com.hychen11.order.entity.OrderEntity;
import com.hychen11.order.service.OrderService;

import com.hychen11.order.interceptor.LoginUserInterceptor;
import com.hychen11.order.service.OrderItemService;

@Service("orderService")
public class OrderServiceImpl extends ServiceImpl<OrderDao, OrderEntity> implements OrderService {
    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private OrderDao orderDao;


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<OrderEntity> page = this.page(
                new Query<OrderEntity>().getPage(params),
                new QueryWrapper<OrderEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPageWithItem(Map<String, Object> params) {
        MemberTo member = LoginUserInterceptor.loginUser.get();
        Long memberId = member.getId();
        IPage<OrderEntity> page = this.page(
                new Query<OrderEntity>().getPage(params),
                new LambdaQueryWrapper<OrderEntity>()
                        .eq(OrderEntity::getMemberId,memberId).orderByDesc(OrderEntity::getId));
        if(page!=null){
            List<OrderEntity> orderList = page.getRecords().stream().map(order -> {
                List<OrderItemEntity> orderItems = orderItemService.list(new LambdaQueryWrapper<OrderItemEntity>()
                        .eq(OrderItemEntity::getOrderSn, order.getOrderSn()));
                order.setOrderItems(orderItems);
                return order;
            }).toList();
            page.setRecords(orderList);
            return new PageUtils(page);
        }
        return null;
    }

    @Override
    public OrderEntity getOrderByOrderSn(String orderSn) {
        return orderDao.selectOne(new LambdaQueryWrapper<OrderEntity>()
                .eq(OrderEntity::getOrderSn, orderSn));
    }

}