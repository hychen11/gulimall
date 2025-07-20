package com.hychen11.ware.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.hychen11.ware.vo.MergeVo;
import com.hychen11.ware.vo.PurchaseFinishVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.hychen11.ware.entity.PurchaseEntity;
import com.hychen11.ware.service.PurchaseService;
import com.hychen11.common.utils.PageUtils;
import com.hychen11.common.utils.R;



/**
 * 采购信息
 *
 * @author hychen11
 * @email 
 * @date 2025-02-10 16:18:40
 */
@RestController
@RequestMapping("ware/purchase")
public class PurchaseController {
    @Autowired
    private PurchaseService purchaseService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = purchaseService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 查询未领取的采购单
     *
     * @return
     */
    @GetMapping("/unreceive/list")
    public R queryPageUnreceivePurchase(@RequestParam Map<String, Object> params) {
        PageUtils page = purchaseService.queryPageUnreceivePurchase(params);
        return R.ok().put("page", page);
    }

    /**
     * 合并采购需求到采购单
     * @param mergeVo
     * @return
     */
    @PostMapping("merge")
    public R merge(@RequestBody MergeVo mergeVo){
        purchaseService.merge(mergeVo);
        return R.ok();
    }

    /**
     * 领取采购单
     * @param ids
     * @return
     */
    @PostMapping("received")
    public R received(@RequestBody List<Long> ids){
        purchaseService.received(ids);
        return R.ok();
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		PurchaseEntity purchase = purchaseService.getById(id);

        return R.ok().put("purchase", purchase);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody PurchaseEntity purchase){
        purchase.setCreateTime(new Date());
        purchase.setUpdateTime(new Date());
        purchaseService.save(purchase);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody PurchaseEntity purchase){
        purchase.setUpdateTime(new Date());
		purchaseService.updateById(purchase);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		purchaseService.deleteByIds(Arrays.asList(ids));

        return R.ok();
    }

    /**
     * 完成采购
     * @param finishVo
     * @return
     */
    @PostMapping("/done")
    public R finish(@RequestBody PurchaseFinishVo finishVo){
        purchaseService.finish(finishVo);
        return R.ok();
    }
}
