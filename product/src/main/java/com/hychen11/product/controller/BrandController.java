package com.hychen11.product.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.hychen11.common.valid.AddGroup;
import com.hychen11.common.valid.UpdateGroup;
import com.hychen11.common.valid.UpdateStatusGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hychen11.product.entity.BrandEntity;
import com.hychen11.product.service.BrandService;
import com.hychen11.common.utils.PageUtils;
import com.hychen11.common.utils.R;

import javax.validation.Valid;


/**
 * 品牌
 *
 * @author hychen11
 * @email 
 * @date 2025-02-10 15:49:12
 */
@RestController
@RequestMapping("product/brand")
public class BrandController {
    @Autowired
    private BrandService brandService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = brandService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{brandId}")
    public R info(@PathVariable("brandId") Long brandId){
		BrandEntity brand = brandService.getById(brandId);

        return R.ok().put("brand", brand);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@Validated({AddGroup.class}) @RequestBody BrandEntity brand, BindingResult result){
        if(result.hasErrors()){
            Map<String,String> map = new HashMap<>();
            result.getFieldErrors().forEach(item->{String defaultMessage = item.getDefaultMessage();
            String field=item.getField();
            map.put(field,defaultMessage);
            });
            return R.error(400,"illegal data").put("data",map);
        }
		brandService.save(brand);
        return R.ok();
    }

    /**
     * 修改品牌显示状态
     */
    @RequestMapping("/update/status")
    public R updateStatus(@Validated({UpdateStatusGroup.class}) @RequestBody BrandEntity brand){
        brandService.updateById(brand);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@Validated({UpdateGroup.class}) @RequestBody BrandEntity brand){
//		brandService.updateById(brand);
        brandService.updateCascade(brand);
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] brandIds){
		brandService.removeByIds(Arrays.asList(brandIds));

        return R.ok();
    }

}
