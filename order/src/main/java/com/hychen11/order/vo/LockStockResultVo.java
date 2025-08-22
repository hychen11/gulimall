package com.hychen11.order.vo;

import lombok.Data;

/**
 * @author ：hychen11
 * @Description:
 * @ClassName: LockStockResultVo
 * @date ：2025/8/14 18:57
 */
@Data
public class LockStockResultVo {
    private Long skuId;
    private Integer num;
    private Boolean locked;
}
