package com.hychen11.ware.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.hychen11.common.to.SkuHasStockTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.hychen11.ware.entity.WareSkuEntity;
import com.hychen11.ware.service.WareSkuService;
import com.hychen11.common.utils.PageUtils;
import com.hychen11.common.utils.R;



/**
 * 商品库存
 *
 * @author hychen11
 * @email 
 * @date 2025-02-10 16:18:40
 */
@RestController
@RequestMapping("ware/waresku")
public class WareSkuController {
    @Autowired
    private WareSkuService wareSkuService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = wareSkuService.queryPageByCondition(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		WareSkuEntity wareSku = wareSkuService.getById(id);

        return R.ok().put("wareSku", wareSku);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody WareSkuEntity wareSku){
		wareSkuService.save(wareSku);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody WareSkuEntity wareSku){
		wareSkuService.updateById(wareSku);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		wareSkuService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

    /**
     * 查询sku是否有库存
     * @param skuIds
     * @return
     */
    @PostMapping("/hasStock")
    public List<SkuHasStockTo> getSkusHasStock(@RequestBody List<Long> skuIds) {
        return wareSkuService.getSkusHasStock(skuIds);
    }

    @GetMapping("{skuId}/hasStock")
    public boolean hasStockBySkuId(@PathVariable Long skuId) {
        return wareSkuService.hasStockBySkuId(skuId);
    }
}
