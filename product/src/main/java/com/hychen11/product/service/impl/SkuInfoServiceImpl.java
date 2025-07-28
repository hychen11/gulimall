package com.hychen11.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hychen11.product.dao.SkuImagesDao;
import com.hychen11.product.dao.SpuInfoDescDao;
import com.hychen11.product.entity.SkuImagesEntity;
import com.hychen11.product.entity.SpuInfoDescEntity;
import com.hychen11.product.feign.WareFeignService;
import com.hychen11.product.service.AttrGroupService;
import com.hychen11.product.service.SkuSaleAttrValueService;
import com.hychen11.product.vo.SkuItemSaleAttrsVo;
import com.hychen11.product.vo.SkuItemVo;
import com.hychen11.product.vo.SpuItemAttrGroupVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hychen11.common.utils.PageUtils;
import com.hychen11.common.utils.Query;

import com.hychen11.product.dao.SkuInfoDao;
import com.hychen11.product.entity.SkuInfoEntity;
import com.hychen11.product.service.SkuInfoService;

import javax.annotation.Resource;


@Service("skuInfoService")
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoDao, SkuInfoEntity> implements SkuInfoService {
    @Resource
    private SkuInfoDao skuInfoDao;
    @Resource
    private SkuImagesDao skuImagesDao;
    @Resource
    private SpuInfoDescDao spuInfoDescDao;
    @Resource
    private SkuSaleAttrValueService skuSaleAttrValueService;
    @Resource
    private AttrGroupService attrGroupService;
    @Resource
    private WareFeignService wareFeignService;
    @Resource
    private ThreadPoolExecutor executor;

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

    /**
     * 根据spuId查sku
     *
     * @param spuId
     * @return
     */
    @Override
    public List<SkuInfoEntity> getSkuBySpuId(Long spuId) {
        LambdaQueryWrapper<SkuInfoEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SkuInfoEntity::getSkuId, spuId);
        List<SkuInfoEntity> skuInfoEntityLits = skuInfoDao.selectList(wrapper);
        return skuInfoEntityLits;
    }

    /**
     * 用户端商品详情查询
     *
     * @param skuId
     * @return
     */
    @Override
    public SkuItemVo item(Long skuId) throws ExecutionException, InterruptedException {
        SkuItemVo skuItemVo = new SkuItemVo();
        CompletableFuture<SkuInfoEntity> skuInfoFuture = CompletableFuture.supplyAsync(() -> {
            //1.sku的基本信息获取      pms_sku_info
            SkuInfoEntity skuInfo = skuInfoDao.selectOne(new LambdaQueryWrapper<SkuInfoEntity>().eq(SkuInfoEntity::getSkuId, skuId));
            skuItemVo.setSkuInfo(skuInfo);
            return skuInfo;
        }, executor);
        CompletableFuture<Void> saleAttrFuture = skuInfoFuture.thenAcceptAsync(res -> {
            //3.spu的销售属性组合
            List<SkuItemSaleAttrsVo> saleAttrs = skuSaleAttrValueService.getSaleAttrsBySpuId(res.getSpuId());
            skuItemVo.setSaleAttrs(saleAttrs);
        }, executor);
        CompletableFuture<Void> descFuture = skuInfoFuture.thenAcceptAsync(res -> {
            //4.spu的介绍           pms_spu_info_desc
            SpuInfoDescEntity spuInfoDesc = spuInfoDescDao.selectOne(new LambdaQueryWrapper<SpuInfoDescEntity>().eq(SpuInfoDescEntity::getSpuId, res.getSpuId()));
            skuItemVo.setSpuInfoDesc(spuInfoDesc);
        }, executor);
        CompletableFuture<Void> groupFuture = skuInfoFuture.thenAcceptAsync((res) -> {
            Long spuId = res.getSpuId();
            Long catalogId = res.getCatalogId();
            //5.spu的规格参数信息
            List<SpuItemAttrGroupVo> attrGroups = attrGroupService.getAttrGroupWithAttrsBySpuId(spuId, catalogId);
            skuItemVo.setAttrGroups(attrGroups);
        }, executor);
        CompletableFuture<Void> hasStockFuture = CompletableFuture.runAsync(() -> {
            //6.远程调用查库存信息
            boolean b = wareFeignService.hasStockBySkuId(skuId);
            skuItemVo.setHasStock(b);
        }, executor);
        CompletableFuture<Void> imagesFuture = CompletableFuture.runAsync(() -> {
            //2.sku的图片信息        pms_sku_images
            List<SkuImagesEntity> skuImages = skuImagesDao.selectList(new LambdaQueryWrapper<SkuImagesEntity>().eq(SkuImagesEntity::getSkuId, skuId));
            skuItemVo.setSkuImages(skuImages);
        }, executor);
        //等待所有任务都完成
        CompletableFuture.allOf(saleAttrFuture, descFuture, groupFuture, hasStockFuture, imagesFuture).get();

        return skuItemVo;
    }
}