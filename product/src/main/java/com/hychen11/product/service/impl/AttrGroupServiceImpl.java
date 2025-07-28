package com.hychen11.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hychen11.product.dao.AttrAttrgroupRelationDao;
import com.hychen11.product.dao.AttrDao;
import com.hychen11.product.entity.AttrAttrgroupRelationEntity;
import com.hychen11.product.entity.AttrEntity;
import com.hychen11.product.vo.AttrGroupWithAttrsVo;
import com.hychen11.product.vo.SpuItemAttrGroupVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hychen11.common.utils.PageUtils;
import com.hychen11.common.utils.Query;

import com.hychen11.product.dao.AttrGroupDao;
import com.hychen11.product.entity.AttrGroupEntity;
import com.hychen11.product.service.AttrGroupService;



@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {

    @Autowired
    private AttrGroupDao attrGroupDao;
    @Autowired
    private AttrAttrgroupRelationDao relationDao;
    @Autowired
    private AttrDao attrDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrGroupEntity> page = this.page(
                new Query<AttrGroupEntity>().getPage(params),
                new QueryWrapper<AttrGroupEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPageByCatelogId(Map<String, Object> params,Long catelogId){
        String key = (String) params.get("key");
        LambdaQueryWrapper<AttrGroupEntity> wrapper = new LambdaQueryWrapper<>();
        if (!StringUtils.isEmpty(key)) {
            wrapper.and((obj) -> obj.eq(AttrGroupEntity::getAttrGroupId, key).or()
                    .like(AttrGroupEntity::getAttrGroupName,key));
        }
        if (catelogId == 0) {
            IPage<AttrGroupEntity> page = this.page(new Query<AttrGroupEntity>().getPage(params),wrapper);
            return new PageUtils(page);
        } else {
            wrapper.eq(AttrGroupEntity::getCatelogId, catelogId);
            IPage<AttrGroupEntity> page = this.page(new Query<AttrGroupEntity>().getPage(params), wrapper);
            return new PageUtils(page);
        }
    }

    @Override
    public List<AttrGroupWithAttrsVo> getGroupWithAttr(Long catelogId){
        //1.查出当前分类下的所有属性分组
        List<AttrGroupEntity> attrGroups = attrGroupDao.selectList(new LambdaQueryWrapper<AttrGroupEntity>()
                .eq(AttrGroupEntity::getCatelogId, catelogId));
        //2.查出每个属性分组的所有属性
        if (!attrGroups.isEmpty()) {
            //取出属性分组的id
            List<Long> attrGroupIds = attrGroups.stream().map(AttrGroupEntity::getAttrGroupId).toList();
            //stream遍历这些ids并返回vos
            List<AttrGroupWithAttrsVo> vos = attrGroupIds.stream().map((attrGroupId) -> {
                //从relation按照attrGroup查询出实体列表
                List<AttrAttrgroupRelationEntity> relationEntities = relationDao.selectList(new LambdaQueryWrapper<AttrAttrgroupRelationEntity>()
                        .eq(AttrAttrgroupRelationEntity::getAttrGroupId, attrGroupId));
                if (relationEntities.isEmpty()) {
                    return null; // 直接跳过，没有关联属性的分组
                }
                AttrGroupWithAttrsVo vo = new AttrGroupWithAttrsVo();
                List<Long> attrIds = relationEntities.stream().map(AttrAttrgroupRelationEntity::getAttrId).toList();
                List<AttrEntity> attrs = attrDao.selectBatchIds(attrIds);
                AttrGroupEntity attrGroup = attrGroupDao.selectById(attrGroupId);
                BeanUtils.copyProperties(attrGroup, vo);
                vo.setAttrs(attrs);
                return vo;
            }).filter(Objects::nonNull).toList();
            return vos;
        }
        return null;
    }

    /**
     * 用户端商品详情，根据spuId查询属性分组及其对应属性
     * @param spuId
     * @param catalogId
     * @return
     */
    @Override
    public List<SpuItemAttrGroupVo> getAttrGroupWithAttrsBySpuId(Long spuId, Long catalogId) {
        return attrDao.getAttrGroupWithAttrsBySpuId(spuId,catalogId);
    }

}