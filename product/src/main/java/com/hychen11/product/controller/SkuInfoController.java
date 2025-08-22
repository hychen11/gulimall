package com.hychen11.product.controller;

import java.util.Arrays;
import java.util.Map;

import com.hychen11.common.to.SkuInfoTo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.hychen11.product.entity.SkuInfoEntity;
import com.hychen11.product.service.SkuInfoService;
import com.hychen11.common.utils.PageUtils;
import com.hychen11.common.utils.R;



/**
 * sku信息
 *
 * @author hychen11
 * @email
 * @date 2025-02-10 15:49:12
 */
@RestController
@RequestMapping("product/skuinfo")
public class SkuInfoController {
    @Autowired
    private SkuInfoService skuInfoService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = skuInfoService.queryPageByCondition(params);

        return R.ok().put("page", page);
    }

    /**
     * 远程调用openFeign查询skuInfo
     * @param skuId
     * @return
     */
    @RequestMapping("infoBySkuId")
    public SkuInfoTo infoBySkuId(@RequestParam Long skuId){
        SkuInfoEntity skuInfo = skuInfoService.getById(skuId);
        SkuInfoTo skuInfoTo = new SkuInfoTo();
        BeanUtils.copyProperties(skuInfo,skuInfoTo);
        return skuInfoTo;
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{skuId}")
    public R info(@PathVariable("skuId") Long skuId){
		SkuInfoEntity skuInfo = skuInfoService.getById(skuId);

        return R.ok().put("skuInfo", skuInfo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody SkuInfoEntity skuInfo){
		skuInfoService.save(skuInfo);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody SkuInfoEntity skuInfo){
		skuInfoService.updateById(skuInfo);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] skuIds){
		skuInfoService.removeByIds(Arrays.asList(skuIds));

        return R.ok();
    }

}
