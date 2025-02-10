package com.hychen11.product.dao;

import com.hychen11.product.entity.CategoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品三级分类
 * 
 * @author hychen11
 * @email 
 * @date 2025-02-10 15:49:12
 */
@Mapper
public interface CategoryDao extends BaseMapper<CategoryEntity> {
	
}
