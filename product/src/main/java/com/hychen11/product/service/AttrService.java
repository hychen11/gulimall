package com.hychen11.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hychen11.common.utils.PageUtils;
import com.hychen11.product.entity.AttrEntity;
import com.hychen11.product.vo.AttrVo;
import com.hychen11.product.vo.AttrRespVo;
import com.hychen11.product.vo.AttrGroupRelationVo;

import java.util.List;
import java.util.Map;

/**
 * 商品属性
 *
 * @author hychen11
 * @email 
 * @date 2025-02-10 15:49:12
 */
public interface AttrService extends IService<AttrEntity> {


    void saveAttr(AttrVo attr);

    List<AttrEntity> getAttrRelation(Long attrGroupId);

    void deleteRelation(AttrGroupRelationVo[] relationVos);

    PageUtils noRelaitonList(Map<String, Object> params, Long attrGroupId);

    AttrRespVo getAttrInfo(Long attrId);

    void updateCascade(AttrVo attr);

    void removeCascade(List<Long> list);

    PageUtils queryPage(Map<String, Object> params, String attrType, Long catelogId);
}

