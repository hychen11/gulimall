package com.hychen11.order.to;

import com.hychen11.order.entity.OrderEntity;
import com.hychen11.order.entity.OrderItemEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author ：hychen11
 * @Description:
 * @ClassName: OrderCreateTo
 * @date ：2025/8/14 19:02
 */
@Data
public class OrderCreateTo {
    private OrderEntity order;
    private List<OrderItemEntity> orderItem;
    private BigDecimal payPrice; //应付价格
    private BigDecimal fare; //运费
}
