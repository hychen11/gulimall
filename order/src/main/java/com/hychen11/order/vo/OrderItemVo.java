package com.hychen11.order.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author ：hychen11
 * @Description:
 * @ClassName: OrderItemVo
 * @date ：2025/8/12 22:13
 */
@Data
public class OrderItemVo {
    private Long skuId;

    private String title;

    private String image;

    private List<String> skuAttr;

    private BigDecimal price;

    private Integer count;

    // totalPrice = price * count
    private BigDecimal totalPrice;
    private BigDecimal weight;
}
