package com.hychen11.common.to;

import lombok.Data;

/**
 * @author ：hychen11
 * @Description:
 * @ClassName: SkuHasStockTo
 * @date ：2025/7/20 17:56
 */
@Data
public class SkuHasStockTo {
    private Long SkuId;
    private Boolean hasStock;
}
