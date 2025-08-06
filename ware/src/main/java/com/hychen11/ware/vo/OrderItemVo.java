package com.hychen11.ware.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author ：hychen11
 * @Description:
 * @ClassName: OrderItemVo
 * @date ：2025/8/5 21:58
 */
@Data
public class OrderItemVo {
    private Long skuId;

    private String title;

    private String image;

    private List<String> skuAttr;

    private BigDecimal price;

    private Integer count;
    private BigDecimal totalPrice;
    private BigDecimal weight;
}
