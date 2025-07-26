package com.hychen11.ware.vo;

import lombok.Data;

import java.util.List;

/**
 * @author ：hychen11
 * @Description:
 * @ClassName: MergeVo
 * @date ：2025/7/20 16:54
 */
@Data
public class MergeVo {
    private Long purchaseId;
    private List<Long> items;
}
