package com.hychen11.product.dao;

import com.hychen11.product.entity.AttrAttrgroupRelationEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 属性&属性分组关联
 * 
 * @author hychen11
 * @email 
 * @date 2025-02-10 15:49:12
 */
@Mapper
public interface AttrAttrgroupRelationDao extends BaseMapper<AttrAttrgroupRelationEntity> {

    void deleteBatchRelation(@Param("list") List<AttrAttrgroupRelationEntity> list);

    void insertBatch(@Param("relations") List<AttrAttrgroupRelationEntity> relations);
}
