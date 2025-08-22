package com.hychen11.order.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author ：hychen11
 * @Description:
 * @ClassName: FareVo
 * @date ：2025/8/14 18:57
 */
@Data
public class FareVo {
    private MemberAddressVo address;
    private BigDecimal fare;
}
