package com.hychen11.ware.dao;

import com.hychen11.ware.entity.WareSkuEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品库存
 * 
 * @author hychen11
 * @email 
 * @date 2025-02-10 16:18:40
 */
@Mapper
public interface WareSkuDao extends BaseMapper<WareSkuEntity> {

    void addStock(WareSkuEntity wareSkuEntity);

    Long getSkuStock(Long skuId);

    List<Long> listWareHasStock(@Param("skuId") Long skuId);

    void releaseLocked(@Param("skuId") Long skuId, @Param("wareId") Long wareId, @Param("skuNum") Integer skuNum);

    Long lockSkuStock(@Param("skuId") Long skuId, @Param("wareId") Long wareId, @Param("skuNum") Integer skuNum);
}
