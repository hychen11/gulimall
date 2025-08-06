package com.hychen11.ware.vo;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author ：hychen11
 * @Description:
 * @ClassName: SkuWareHasStock
 * @date ：2025/8/5 22:26
 */
@Data
@Builder
public class SkuWareHasStock {
    private Long skuId;
    private List<Long> wareIds;
    /** 购买数量 */
    private Integer skuNum;
    private String skuName;
}
