package com.hychen11.order.vo;

import lombok.Data;

/**
 * @author ：hychen11
 * @Description:
 * @ClassName: SkuHasStockVo
 * @date ：2025/8/12 22:21
 */
@Data
public class SkuHasStockVo {
    private Long SkuId;
    private Boolean hasStock;
}
