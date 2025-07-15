package com.hychen11.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hychen11.common.utils.PageUtils;
import com.hychen11.product.entity.AttrAttrgroupRelationEntity;
import com.hychen11.product.vo.AttrGroupRelationVo;

import java.util.Map;

/**
 * 属性&属性分组关联
 *
 * @author hychen11
 * @email 
 * @date 2025-02-10 15:49:12
 */
public interface AttrAttrgroupRelationService extends IService<AttrAttrgroupRelationEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void addAttrRelaiton(AttrGroupRelationVo[] relationVos);
}

