package com.hychen11.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hychen11.common.to.SkuHasStockTo;
import com.hychen11.common.to.OrderTo;
import com.hychen11.common.utils.PageUtils;
import com.hychen11.ware.entity.WareOrderTaskDetailEntity;
import com.hychen11.ware.entity.WareOrderTaskEntity;
import com.hychen11.ware.entity.WareSkuEntity;
import com.hychen11.ware.vo.WareSkuLockVo;

import java.util.List;
import java.util.Map;

/**
 * 商品库存
 *
 * @author hychen11
 * @email 
 * @date 2025-02-10 16:18:40
 */
public interface WareSkuService extends IService<WareSkuEntity> {

    PageUtils queryPage(Map<String, Object> params);

    PageUtils queryPageByCondition(Map<String, Object> params);

    void addStock(WareSkuEntity wareSkuEntity);

    List<SkuHasStockTo> getSkusHasStock(List<Long> skuIds);

    boolean hasStockBySkuId(Long skuId);

    Boolean orderLockStock(WareSkuLockVo vo);

    void unlockStock(OrderTo order);

    void unlockStock(WareOrderTaskDetailEntity taskDetailEntity);
}

