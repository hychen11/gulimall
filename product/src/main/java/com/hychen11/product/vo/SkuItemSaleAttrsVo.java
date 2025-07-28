package com.hychen11.product.vo;

import lombok.Data;

import java.util.List;

/**
 * @author ：hychen11
 * @Description:
 * @ClassName: SkuItemSaleAttrsVo
 * @date ：2025/7/29 00:23
 */
@Data
public class SkuItemSaleAttrsVo {
    private Long attrId;
    private String attrName;
    private List<AttrValueWithSkuIdVo> attrValues;
}
