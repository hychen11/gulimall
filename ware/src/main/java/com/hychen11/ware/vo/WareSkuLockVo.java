package com.hychen11.ware.vo;

import lombok.Data;

import java.util.List;

/**
 * @author ：hychen11
 * @Description:
 * @ClassName: WareSkuLockVo
 * @date ：2025/8/5 13:36
 */
@Data
public class WareSkuLockVo {
    private String orderSn;
    private List<OrderItemVo> orderItems; //需要锁的库存信息
}
