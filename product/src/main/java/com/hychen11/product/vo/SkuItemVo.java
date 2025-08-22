package com.hychen11.product.vo;

import com.hychen11.product.entity.SkuImagesEntity;
import com.hychen11.product.entity.SkuInfoEntity;
import com.hychen11.product.entity.SpuInfoDescEntity;
import lombok.Data;

import java.util.List;

/**
 * @author ：hychen11
 * @Description:
 * @ClassName: SkuItemVo
 * @date ：2025/7/29 00:24
 */
@Data
public class SkuItemVo {
    private SkuInfoEntity skuInfo;
    private boolean hasStock = true;
    private List<SkuImagesEntity> skuImages;
    private List<SkuItemSaleAttrsVo> saleAttrs;
    private SpuInfoDescEntity spuInfoDesc;
    private List<SpuItemAttrGroupVo> attrGroups;
    private SeckillInfoVo seckillInfo;
}
