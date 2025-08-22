package com.hychen11.order.vo;

import lombok.Data;

import java.util.List;

/**
 * @author ：hychen11
 * @Description:
 * @ClassName: WareSkuLockVo
 * @date ：2025/8/12 22:30
 */
@Data
public class WareSkuLockVo {
    private String orderSn;
    //需要锁的库存信息
    private List<OrderItemVo> locks;
}
