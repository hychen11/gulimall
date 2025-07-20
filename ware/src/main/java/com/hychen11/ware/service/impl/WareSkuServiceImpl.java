package com.hychen11.ware.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hychen11.common.to.SkuHasStockTo;
import com.hychen11.common.to.SkuInfoTo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hychen11.common.utils.PageUtils;
import com.hychen11.common.utils.Query;

import com.hychen11.ware.dao.WareSkuDao;
import com.hychen11.ware.entity.WareSkuEntity;
import com.hychen11.ware.service.WareSkuService;


@Service("wareSkuService")
public class WareSkuServiceImpl extends ServiceImpl<WareSkuDao, WareSkuEntity> implements WareSkuService {
    @Autowired
    public WareSkuDao wareSkuDao;
    @Autowired
    public com.hychen11.ware.feign.ProductFeign productFeign;


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<WareSkuEntity> page = this.page(
                new Query<WareSkuEntity>().getPage(params),
                new QueryWrapper<WareSkuEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 条件分页查询
     *
     * @param params
     * @return
     */
    @Override
    public PageUtils queryPageByCondition(Map<String, Object> params) {
        LambdaQueryWrapper<WareSkuEntity> wrapper = new LambdaQueryWrapper<>();
        String skuId = (String) params.get("skuId");
        String wareId = (String) params.get("wareId");
        if (!StringUtils.isEmpty(skuId)) {
            wrapper.eq(WareSkuEntity::getSkuId, skuId);
        }
        if (!StringUtils.isEmpty(wareId)) {
            wrapper.eq(WareSkuEntity::getWareId, wareId);
        }
        IPage<WareSkuEntity> page = this.page(
                new Query<WareSkuEntity>().getPage(params),
                wrapper
        );

        return new PageUtils(page);
    }

    @Override
    public void addStock(WareSkuEntity wareSkuEntity){
        //判断如果没有库存记录就是新增操作
        LambdaQueryWrapper<WareSkuEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(WareSkuEntity::getSkuId, wareSkuEntity.getSkuId());
        wrapper.eq(WareSkuEntity::getWareId, wareSkuEntity.getWareId());
        List<WareSkuEntity> wareSkuEntities = wareSkuDao.selectList(wrapper);
        if (wareSkuEntities == null || wareSkuEntities.isEmpty()) {
            wareSkuEntity.setStockLocked(0);
            //远程查询sku的名字
            try {
                SkuInfoTo skuInfoTo = productFeign.infoBySkuId(wareSkuEntity.getSkuId());
                if (skuInfoTo != null) {
                    wareSkuEntity.setSkuName(skuInfoTo.getSkuName());
                }
            } catch (Exception e) {

            }
            wareSkuDao.insert(wareSkuEntity);
        } else {
            wareSkuDao.addStock(wareSkuEntity);
        }
    }

    /**
     * 查询sku是否有库存
     *
     * @param skuIds
     * @return
     */
    @Override
    public List<SkuHasStockTo> getSkusHasStock(List<Long> skuIds){
        List<SkuHasStockTo> tos = skuIds.stream().map(skuId -> {
            SkuHasStockTo skuHasStockTo = new SkuHasStockTo();
            Long stock = wareSkuDao.getSkuStock(skuId);
            skuHasStockTo.setHasStock(stock == null ? false : stock > 0);
            skuHasStockTo.setSkuId(skuId);
            return skuHasStockTo;
        }).toList();
        return tos;
    }

}