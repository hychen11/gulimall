package com.hychen11.common.to;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author ：hychen11
 * @Description:
 * @ClassName: MemberPrice
 * @date ：2025/7/18 09:26
 */
@Data
public class MemberPrice {
    private Long id;
    private String name;
    private BigDecimal price;
}
