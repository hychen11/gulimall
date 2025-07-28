package com.hychen11.product.service.impl;

import com.hychen11.product.vo.SkuItemSaleAttrsVo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hychen11.common.utils.PageUtils;
import com.hychen11.common.utils.Query;

import com.hychen11.product.dao.SkuSaleAttrValueDao;
import com.hychen11.product.entity.SkuSaleAttrValueEntity;
import com.hychen11.product.service.SkuSaleAttrValueService;

import javax.annotation.Resource;


@Service("skuSaleAttrValueService")
public class SkuSaleAttrValueServiceImpl extends ServiceImpl<SkuSaleAttrValueDao, SkuSaleAttrValueEntity> implements SkuSaleAttrValueService {
    @Resource
    private SkuSaleAttrValueDao skuSaleAttrValueDao;
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuSaleAttrValueEntity> page = this.page(
                new Query<SkuSaleAttrValueEntity>().getPage(params),
                new QueryWrapper<SkuSaleAttrValueEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 用户端，商品详情，获取销售属性
     * @param spuId
     * @return
     */
    @Override
    public List<SkuItemSaleAttrsVo> getSaleAttrsBySpuId(Long spuId) {
        return skuSaleAttrValueDao.getSaleAttrsBySpuId(spuId);
    }

}