package com.hychen11.ware.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author ：hychen11
 * @Description:
 * @ClassName: FareVo
 * @date ：2025/8/5 13:37
 */
@Data
public class FareVo {
    private MemberAddressVo address;
    private BigDecimal fare;
}
