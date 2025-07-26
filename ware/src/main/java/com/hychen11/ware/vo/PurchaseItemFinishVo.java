package com.hychen11.ware.vo;

import lombok.Data;

/**
 * @author ：hychen11
 * @Description:
 * @ClassName: PurchaseItemFinishVo
 * @date ：2025/7/20 17:03
 */
@Data
public class PurchaseItemFinishVo {
    private Long itemId;
    private Integer status;
    private String reason;
}
