package com.hychen11.coupon.service.impl;

import com.alibaba.nacos.common.utils.CollectionUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hychen11.coupon.dao.SeckillSkuRelationDao;
import com.hychen11.coupon.entity.SeckillSkuRelationEntity;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hychen11.common.utils.PageUtils;
import com.hychen11.common.utils.Query;

import com.hychen11.coupon.dao.SeckillSessionDao;
import com.hychen11.coupon.entity.SeckillSessionEntity;
import com.hychen11.coupon.service.SeckillSessionService;


@Service("seckillSessionService")
public class SeckillSessionServiceImpl extends ServiceImpl<SeckillSessionDao, SeckillSessionEntity> implements SeckillSessionService {
    @Autowired
    private SeckillSessionDao seckillSessionDao;
    @Autowired
    private SeckillSkuRelationDao seckillSkuRelationDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        LambdaQueryWrapper<SeckillSessionEntity> wrapper = new LambdaQueryWrapper<>();
        String key = (String) params.get("key");
        if (!StringUtils.isBlank(key)) {
            wrapper.like(SeckillSessionEntity::getName, key).or()
                    .like(SeckillSessionEntity::getId, key);
        }
        IPage<SeckillSessionEntity> page = this.page(
                new Query<SeckillSessionEntity>().getPage(params),
                wrapper
        );

        return new PageUtils(page);
    }

    @Override
    public List<SeckillSessionEntity> getLatest3DaySession() {
        LocalDate now = LocalDate.now();
        LocalDate plus = now.plusDays(2);
        LocalTime min = LocalTime.MIN;
        LocalTime max = LocalTime.MAX;

        LocalDateTime start = LocalDateTime.of(now, min);
        LocalDateTime end = LocalDateTime.of(plus, max);

        String formatStart = start.format(DateTimeFormatter.ofPattern("yyyy-MM-dd H:mm:ss"));
        String formatEnd = end.format(DateTimeFormatter.ofPattern("yyyy-MM-dd H:mm:ss"));
        LambdaQueryWrapper<SeckillSessionEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.between(SeckillSessionEntity::getStartTime, formatStart, formatEnd);


        List<SeckillSessionEntity> seckillSessions = seckillSessionDao.selectList(wrapper);
        if (!CollectionUtils.isEmpty(seckillSessions)) {
            return seckillSessions.stream().map(session -> {
                Long sessionId = session.getId();
                List<SeckillSkuRelationEntity> relations = seckillSkuRelationDao.selectList(new LambdaQueryWrapper<SeckillSkuRelationEntity>()
                        .eq(SeckillSkuRelationEntity::getPromotionSessionId, sessionId));
                if (!CollectionUtils.isEmpty(relations)) {
                    session.setRelationSkus(relations);
                }
                return session;
            }).toList();
        }
        return null;
    }

}