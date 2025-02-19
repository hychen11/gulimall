package com.hychen11.product.service.impl;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hychen11.common.utils.PageUtils;
import com.hychen11.common.utils.Query;

import com.hychen11.product.dao.CategoryDao;
import com.hychen11.product.entity.CategoryEntity;
import com.hychen11.product.service.CategoryService;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CategoryEntity> listWithTree() {
        /**
         * need Dao and Entity!
         */
        List<CategoryEntity> categoryEntities = baseMapper.selectList(null);

        /**
         * get tree structure
         */

        List<CategoryEntity> collect = categoryEntities.stream().filter((entity) -> {
            return entity.getParentCid() == 0;
        }).map((menu)->{menu.setChild(getChild(menu, categoryEntities)); return menu;})
                .sorted((menu1,menu2)->{
                    return (menu1.getSort()==null?0:menu1.getSort()) - (menu2.getSort()==null?0:menu2.getSort());
                })
                .collect(Collectors.toList());
        return collect;
    }

    @Override
    public void removeMenuByIds(List<Long> list) {
        // TODO:检查当前删除的菜单，是否被别的地方引用。
        baseMapper.deleteBatchIds(list);
    }

    private List<CategoryEntity> getChild(CategoryEntity root, List<CategoryEntity> all) {
        List<CategoryEntity> collect = all.stream()
                .filter(entity -> entity.getParentCid() == root.getCatId())
                .map(menu -> {menu.setChild(getChild(menu, all)); return menu;})
                .sorted((menu1, menu2) -> {
                    return (menu1.getSort()==null?0:menu1.getSort()) - (menu2.getSort()==null?0:menu2.getSort());
                })
                .collect(Collectors.toList());
        return collect;
    }

}