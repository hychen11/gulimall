package com.hychen11.product.service.impl;

import com.hychen11.product.vo.AttrGroupRelationVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hychen11.common.utils.PageUtils;
import com.hychen11.common.utils.Query;

import com.hychen11.product.dao.AttrAttrgroupRelationDao;
import com.hychen11.product.entity.AttrAttrgroupRelationEntity;
import com.hychen11.product.service.AttrAttrgroupRelationService;

import javax.annotation.Resource;


@Service("attrAttrgroupRelationService")
public class AttrAttrgroupRelationServiceImpl extends ServiceImpl<AttrAttrgroupRelationDao, AttrAttrgroupRelationEntity> implements AttrAttrgroupRelationService {

    @Resource
    private AttrAttrgroupRelationDao relationDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrAttrgroupRelationEntity> page = this.page(
                new Query<AttrAttrgroupRelationEntity>().getPage(params),
                new QueryWrapper<AttrAttrgroupRelationEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void addAttrRelaiton(AttrGroupRelationVo[] relationVos){
        List<AttrAttrgroupRelationEntity> relations = Arrays.stream(relationVos).toList()
                .stream().map((relationVo) -> {
                    AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
                    BeanUtils.copyProperties(relationVo, relationEntity);
                    return relationEntity;
                }).toList();
        relationDao.insertBatch(relations);
    }

}