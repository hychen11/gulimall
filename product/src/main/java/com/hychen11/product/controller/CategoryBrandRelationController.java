package com.hychen11.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.hychen11.product.entity.BrandEntity;
import com.hychen11.product.vo.BrandVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.EnableDiscoveryClientImportSelector;
import org.springframework.web.bind.annotation.*;

import com.hychen11.product.entity.CategoryBrandRelationEntity;
import com.hychen11.product.service.CategoryBrandRelationService;
import com.hychen11.common.utils.PageUtils;
import com.hychen11.common.utils.R;



/**
 * 品牌分类关联
 *
 * @author hychen11
 * @email 
 * @date 2025-02-10 15:49:12
 */
@RestController
@RequestMapping("product/categorybrandrelation")
public class CategoryBrandRelationController {
    @Autowired
    private CategoryBrandRelationService categoryBrandRelationService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = categoryBrandRelationService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
     * 查询分类关联品牌
     * @param catId
     * @return
     */
    @GetMapping("/brands/list")
    public R list(@RequestParam(value = "catId",required = true) Long catId){
        List<BrandEntity> brandEntities = categoryBrandRelationService.brandsList(catId);
        List<BrandVo> brandVos = null;
        if(brandEntities!=null) {
            brandVos = brandEntities.stream().map((brandEntity -> {
                BrandVo brandVo = new BrandVo();
                brandVo.setBrandName(brandEntity.getName());
                brandVo.setBrandId(brandEntity.getBrandId());
                return brandVo;
            })).toList();
        }
        return R.ok().put("data",brandVos);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		CategoryBrandRelationEntity categoryBrandRelation = categoryBrandRelationService.getById(id);

        return R.ok().put("categoryBrandRelation", categoryBrandRelation);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody CategoryBrandRelationEntity categoryBrandRelation){
//		categoryBrandRelationService.save(categoryBrandRelation);
        categoryBrandRelationService.saveDetail(categoryBrandRelation);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody CategoryBrandRelationEntity categoryBrandRelation){
		categoryBrandRelationService.updateById(categoryBrandRelation);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		categoryBrandRelationService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

    @GetMapping("/catelog/list")
    public R catelogList(Long brandId){
        List<CategoryBrandRelationEntity> entities = categoryBrandRelationService.catelogList(brandId);
        return R.ok().put("data",entities);
    }

}
