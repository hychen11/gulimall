package com.hychen11.product.dao;

import com.hychen11.product.entity.SkuSaleAttrValueEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hychen11.product.vo.SkuItemSaleAttrsVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * sku销售属性&值
 * 
 * @author hychen11
 * @email 
 * @date 2025-02-10 15:49:12
 */
@Mapper
public interface SkuSaleAttrValueDao extends BaseMapper<SkuSaleAttrValueEntity> {
    List<SkuItemSaleAttrsVo> getSaleAttrsBySpuId(Long spuId);
}
