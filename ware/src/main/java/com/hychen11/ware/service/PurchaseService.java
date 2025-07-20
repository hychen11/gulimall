package com.hychen11.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hychen11.common.utils.PageUtils;
import com.hychen11.ware.entity.PurchaseEntity;
import com.hychen11.ware.vo.MergeVo;
import com.hychen11.ware.vo.PurchaseFinishVo;

import java.util.List;
import java.util.Map;

/**
 * 采购信息
 *
 * @author hychen11
 * @email 
 * @date 2025-02-10 16:18:40
 */
public interface PurchaseService extends IService<PurchaseEntity> {

    PageUtils queryPage(Map<String, Object> params);

    PageUtils queryPageUnreceivePurchase(Map<String, Object> params);

    void merge(MergeVo mergeVo);

    void received(List<Long> ids);

    void deleteByIds(List<Long> list);

    void finish(PurchaseFinishVo finishVo);
}

