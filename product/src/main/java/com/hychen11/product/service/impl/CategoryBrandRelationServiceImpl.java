package com.hychen11.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hychen11.product.dao.BrandDao;
import com.hychen11.product.dao.CategoryDao;
import com.hychen11.product.entity.BrandEntity;
import com.hychen11.product.entity.CategoryEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hychen11.common.utils.PageUtils;
import com.hychen11.common.utils.Query;

import com.hychen11.product.dao.CategoryBrandRelationDao;
import com.hychen11.product.entity.CategoryBrandRelationEntity;
import com.hychen11.product.service.CategoryBrandRelationService;

import javax.annotation.Resource;


@Service("categoryBrandRelationService")
public class CategoryBrandRelationServiceImpl extends ServiceImpl<CategoryBrandRelationDao, CategoryBrandRelationEntity> implements CategoryBrandRelationService {

    @Resource
    private CategoryBrandRelationDao categoryBrandRelationDao;
    @Resource
    private CategoryDao categoryDao;
    @Resource
    private BrandDao brandDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryBrandRelationEntity> page = this.page(
                new Query<CategoryBrandRelationEntity>().getPage(params),
                new QueryWrapper<CategoryBrandRelationEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CategoryBrandRelationEntity> catelogList(Long brandId){
        LambdaQueryWrapper<CategoryBrandRelationEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CategoryBrandRelationEntity::getBrandId,brandId);
        return categoryBrandRelationDao.selectList(wrapper);
    }

    @Override
    public void saveDetail(CategoryBrandRelationEntity categoryBrandRelation){
        Long brandId = categoryBrandRelation.getBrandId();
        Long catelogId = categoryBrandRelation.getCatelogId();
        BrandEntity brand = brandDao.selectById(brandId);
        CategoryEntity category = categoryDao.selectById(catelogId);
        categoryBrandRelation.setBrandName(brand.getName());
        categoryBrandRelation.setCatelogName(category.getName());
        categoryBrandRelationDao.insert(categoryBrandRelation);
    }

    @Override
    public void updateBrand(Long brandId, String name){
        categoryBrandRelationDao.updateBrand(brandId,name);
    }

    @Override
    public void updateCategory(Long catId, String name){
        categoryBrandRelationDao.updateCategory(catId,name);
    }

}