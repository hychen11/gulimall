package com.hychen11.ware.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author ：chenhaoyang
 * @Description:
 * @ClassName: PurchaseFinishVo
 * @date ：2025/7/20 16:56
 */
@Data
public class PurchaseFinishVo {
    @NotNull
    private Long id;
    private List<PurchaseItemFinishVo> items;
}
