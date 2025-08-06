package com.hychen11.ware.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hychen11.common.utils.PageUtils;
import com.hychen11.common.utils.Query;

import com.hychen11.ware.dao.WareOrderTaskDao;
import com.hychen11.ware.entity.WareOrderTaskEntity;
import com.hychen11.ware.service.WareOrderTaskService;


@Service("wareOrderTaskService")
public class WareOrderTaskServiceImpl extends ServiceImpl<WareOrderTaskDao, WareOrderTaskEntity> implements WareOrderTaskService {
    @Autowired
    private WareOrderTaskDao wareOrderTaskDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<WareOrderTaskEntity> page = this.page(
                new Query<WareOrderTaskEntity>().getPage(params),
                new QueryWrapper<WareOrderTaskEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 根据orderSn查询
     * @param orderSn
     * @return
     */
    @Override
    public WareOrderTaskEntity getOrderTaskByOrderSn(String orderSn) {
        return wareOrderTaskDao.selectOne(new LambdaQueryWrapper<WareOrderTaskEntity>()
                .eq(WareOrderTaskEntity::getOrderSn, orderSn));
    }
}