package com.hychen11.ware.service.impl;

import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hychen11.common.utils.R;
import com.hychen11.ware.feign.MemberFeignService;
import com.hychen11.ware.vo.FareVo;
import com.hychen11.ware.vo.MemberAddressVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hychen11.common.utils.PageUtils;
import com.hychen11.common.utils.Query;

import com.hychen11.ware.dao.WareInfoDao;
import com.hychen11.ware.entity.WareInfoEntity;
import com.hychen11.ware.service.WareInfoService;


@Service("wareInfoService")
public class WareInfoServiceImpl extends ServiceImpl<WareInfoDao, WareInfoEntity> implements WareInfoService {
    @Autowired
    private MemberFeignService memberFeignService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<WareInfoEntity> page = this.page(
                new Query<WareInfoEntity>().getPage(params),
                new QueryWrapper<WareInfoEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 条件分页查询
     *
     * @param params
     * @return
     */
    @Override
    public PageUtils queryPageByCondition(Map<String, Object> params) {
        LambdaQueryWrapper<WareInfoEntity> wrapper = new LambdaQueryWrapper<>();
        String key = (String) params.get("key");
        if (!StringUtils.isEmpty(key)) {
            wrapper.like(WareInfoEntity::getAddress, key).or().like(WareInfoEntity::getName, key)
                    .or().like(WareInfoEntity::getId, key);
        }
        IPage<WareInfoEntity> page = this.page(new Query<WareInfoEntity>().getPage(params), wrapper);
        return new PageUtils(page);
    }

    /**
     * calculate fare fee based on user address
     * */
    @Override
    public FareVo getFare(Long attrId) {
        FareVo fareVo = new FareVo();
        R r = memberFeignService.info(attrId);
        MemberAddressVo address = r.getData2("memberReceiveAddress", new TypeReference<MemberAddressVo>() {
        });
        fareVo.setAddress(address);
        if (address != null) {
            //该地方模拟运费计算
            String phone = address.getPhone();
            String fare = phone.substring(phone.length() - 1);
            BigDecimal b = new BigDecimal(fare);
            fareVo.setFare(b);
            return fareVo;
        }
        return null;
    }
}