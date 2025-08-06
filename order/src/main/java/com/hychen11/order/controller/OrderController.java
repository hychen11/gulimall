package com.hychen11.order.controller;

import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.hychen11.order.entity.OrderEntity;
import com.hychen11.order.service.OrderService;
import com.hychen11.common.utils.PageUtils;
import com.hychen11.common.utils.R;



/**
 * 订单
 *
 * @author hychen11
 * @email 
 * @date 2025-02-10 16:19:38
 */
@RestController
@RequestMapping("order/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    /**
     * 分页查询登陆用户的订单信息
     * @param params
     * @return
     */
    @PostMapping("listWithItem")
    public R listWithItem(@RequestBody Map<String, Object> params){
        PageUtils page = orderService.queryPageWithItem(params);
        return R.ok().put("page", page);
    }

    /**
     * 远程调用，根据orderSn查询订单信息
     * @param orderSn
     * @return
     */

    @GetMapping("/getOrder/{orderSn}")
    public R getOrderByOrderSn(@PathVariable String orderSn){
        OrderEntity order = orderService.getOrderByOrderSn(orderSn);
        return R.ok().setData(order);
    }


    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = orderService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		OrderEntity order = orderService.getById(id);

        return R.ok().put("order", order);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody OrderEntity order){
		orderService.save(order);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody OrderEntity order){
		orderService.updateById(order);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		orderService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
