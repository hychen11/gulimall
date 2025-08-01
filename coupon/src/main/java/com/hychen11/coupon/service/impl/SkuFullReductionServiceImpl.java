package com.hychen11.coupon.service.impl;

import com.hychen11.common.to.MemberPrice;
import com.hychen11.common.to.SkuReductionTo;
import com.hychen11.coupon.entity.MemberPriceEntity;
import com.hychen11.coupon.entity.SkuLadderEntity;
import com.hychen11.coupon.service.MemberPriceService;
import com.hychen11.coupon.service.SkuLadderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hychen11.common.utils.PageUtils;
import com.hychen11.common.utils.Query;

import com.hychen11.coupon.dao.SkuFullReductionDao;
import com.hychen11.coupon.entity.SkuFullReductionEntity;
import com.hychen11.coupon.service.SkuFullReductionService;


@Service("skuFullReductionService")
public class SkuFullReductionServiceImpl extends ServiceImpl<SkuFullReductionDao, SkuFullReductionEntity> implements SkuFullReductionService {

    @Autowired
    private SkuLadderService skuLadderService;
    @Autowired
    private MemberPriceService memberPriceService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuFullReductionEntity> page = this.page(
                new Query<SkuFullReductionEntity>().getPage(params),
                new QueryWrapper<SkuFullReductionEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveSkuReduction(SkuReductionTo skuReductionTo){
        //sku的优惠、满减等信息:gulimall_sms->sms_sku_ladder\sms_sku_full_reduction\sms_member_price\
        //sms_sku_ladder
        SkuLadderEntity skuLadder = new SkuLadderEntity();
        skuLadder.setSkuId(skuReductionTo.getSkuId());
        skuLadder.setFullCount(skuReductionTo.getFullCount());
        skuLadder.setDiscount(skuReductionTo.getDiscount());
        skuLadder.setAddOther(skuReductionTo.getCountStatus());
        if (skuReductionTo.getFullCount() > 0) {
            skuLadderService.save(skuLadder);
        }
        //sms_sku_full_reduction
        SkuFullReductionEntity skuFullReduction = new SkuFullReductionEntity();
        skuFullReduction.setSkuId(skuReductionTo.getSkuId());
        skuFullReduction.setFullPrice(skuReductionTo.getFullPrice());
        skuFullReduction.setReducePrice(skuReductionTo.getReducePrice());
        skuFullReduction.setAddOther(skuReductionTo.getCountStatus());
        if(skuReductionTo.getFullPrice().compareTo(new BigDecimal("0"))==1){
            this.save(skuFullReduction);
        }
        //sms_member_price
        List<MemberPrice> memberPrices = skuReductionTo.getMemberPrice();
        if (memberPrices != null && !memberPrices.isEmpty()) {
            List<MemberPriceEntity> memberPriceEntities = memberPrices.stream().map((item) -> {
                MemberPriceEntity memberPrice = new MemberPriceEntity();
                memberPrice.setSkuId(skuReductionTo.getSkuId());
                memberPrice.setMemberPrice(item.getPrice());
                memberPrice.setMemberLevelId(item.getId());
                memberPrice.setMemberLevelName(item.getName());
                memberPrice.setAddOther(1);
                return memberPrice;
            }).filter(item-> item.getMemberPrice().compareTo(new BigDecimal("0"))==1).toList();
            memberPriceService.saveBatch(memberPriceEntities);
        }
    }

}