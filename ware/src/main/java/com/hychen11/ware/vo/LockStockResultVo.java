package com.hychen11.ware.vo;

import lombok.Data;

/**
 * @author ：hychen11
 * @Description:
 * @ClassName: LockStockResultVo
 * @date ：2025/8/5 22:14
 */
@Data
public class LockStockResultVo {
    private Long skuId;
    private Integer num;
    private Boolean locked;
}
