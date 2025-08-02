package com.hychen11.cart.vo;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author ：hychen11
 * @Description:
 * @ClassName: CartItem
 * @date ：2025/7/31 21:58
 */
@Setter
public class CartItem {
    @Getter
    private Long skuId;
    @Getter
    private boolean check = true;
    @Getter
    private String title;
    @Getter
    private String image;
    @Getter
    private List<String> skuAttr;
    @Getter
    private BigDecimal price;
    @Getter
    private Integer count;
    private BigDecimal totalPrice;

    public BigDecimal getTotalPrice() {
        return new BigDecimal("" + this.count).multiply(this.price);
    }
}
