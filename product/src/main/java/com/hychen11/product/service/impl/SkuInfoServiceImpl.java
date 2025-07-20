package com.hychen11.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hychen11.common.utils.PageUtils;
import com.hychen11.common.utils.Query;

import com.hychen11.product.dao.SkuInfoDao;
import com.hychen11.product.entity.SkuInfoEntity;
import com.hychen11.product.service.SkuInfoService;


@Service("skuInfoService")
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoDao, SkuInfoEntity> implements SkuInfoService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuInfoEntity> page = this.page(
                new Query<SkuInfoEntity>().getPage(params),
                new QueryWrapper<SkuInfoEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPageByCondition(Map<String, Object> params) {
        LambdaQueryWrapper<SkuInfoEntity> wrapper = new LambdaQueryWrapper<>();
        if (!params.isEmpty()) {
            String catelogId = (String) params.get("catelogId");
            if (!StringUtils.isEmpty(catelogId) && !"0".equalsIgnoreCase(catelogId)) {

                wrapper.eq(SkuInfoEntity::getCatalogId, catelogId);
            }
            String brandId = (String) params.get("brandId");
            if (!StringUtils.isEmpty(brandId) && !"0".equalsIgnoreCase(brandId)) {
                wrapper.eq(SkuInfoEntity::getBrandId, brandId);
            }
            String key = (String) params.get("key");
            if (!StringUtils.isEmpty(key)) {
                wrapper.and((w) -> w.like(SkuInfoEntity::getSkuName, key).or().like(SkuInfoEntity::getSkuId, key));
            }
            String min = (String) params.get("min");
            if (!StringUtils.isEmpty(min)) {
                wrapper.ge(SkuInfoEntity::getPrice, min);
            }
            String max = (String) params.get("max");
            if (!StringUtils.isEmpty(max)) {
                try {
                    BigDecimal maxB = new BigDecimal(max);
                    if (maxB.compareTo(new BigDecimal("0")) == 1) {
                        wrapper.le(SkuInfoEntity::getPrice, max);
                    }
                } catch (Exception e) {

                }

            }
        }

        IPage<SkuInfoEntity> page = this.page(new Query<SkuInfoEntity>().getPage(params), wrapper);
        return new PageUtils(page);
    }
}