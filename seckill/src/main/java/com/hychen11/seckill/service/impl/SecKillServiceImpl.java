package com.hychen11.seckill.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.hychen11.common.constant.SeckillConstant;
import com.hychen11.common.to.MemberTo;
import com.hychen11.common.to.mq.SeckillOrderTo;
import com.hychen11.common.utils.R;
import com.hychen11.seckill.interceptor.LoginUserInterceptor;
import com.hychen11.seckill.service.SecKillService;
import com.hychen11.seckill.to.SeckillSkuRedisTo;
import com.hychen11.seckill.vo.SeckillSessionVo;
import com.hychen11.seckill.vo.SeckillSkuRelationVo;
import com.hychen11.seckill.vo.SkuInfoVo;
import org.redisson.api.RSemaphore;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import com.hychen11.seckill.feign.CouponFeignService;
import com.hychen11.seckill.feign.ProductFeignService;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import static com.hychen11.common.constant.SeckillConstant.SKUKILL_CACHE_PREFIX;

/**
 * @author ：hychen11
 * @Description:
 * @ClassName: SecKillServiceImpl
 * @date ：2025/8/23 23:07
 */
@Service
public class SecKillServiceImpl implements SecKillService {
    @Resource
    private CouponFeignService couponFeignService;
    @Resource
    private RedisTemplate<String, String> redisTemplate;
    @Resource
    private ProductFeignService productFeignService;
    @Resource
    private RedissonClient redissonClient;
    @Resource
    private RabbitTemplate rabbitTemplate;


    @Override
    public void uploadSecKillLatest3Days() {
        // 1、扫描最近三天数据库需要参与秒杀的活动
        R session = couponFeignService.getLatest3DaySession();
        if (session.getCode() == 0) {
            // 上架商品
            List<SeckillSessionVo> sessionData = session.getData(new TypeReference<List<SeckillSessionVo>>() {
            });
            // 缓存到Redis
            // 1)、缓存活动信息
            saveSessionInfos(sessionData);
            // 2)、缓存活动的关联商品信息
            saveSessionSkuInfos(sessionData);
        }
    }

    //TODO:逻辑处理不对，ZSET优化

    /**
     * 返回当前时间参与的秒杀商品信息
     *
     * @return
     */
    @Override
    public List<SeckillSkuRedisTo> getCurrentTimeSeckillSkus() {
        //1.确定当前时间属于哪个秒杀场次
        long curTime = new Date().getTime();
        Set<String> keys = redisTemplate.keys(SeckillConstant.SESSIONS_CACHE_PREFIX + "*");
        if (!CollectionUtils.isEmpty(keys)) {
            for (String key : keys) {
                String replace = key.replace(SeckillConstant.SESSIONS_CACHE_PREFIX, "");
                String[] s = replace.split("_");
                long startTime = Long.parseLong(s[0]);
                long endTime = Long.parseLong(s[1]);
                if (curTime >= startTime && curTime <= endTime) {
                    //2.获取这个秒杀场次需要的所有商品信息
                    List<String> sessionSkuIds = redisTemplate.opsForList().range(key, -100, 100);
                    BoundHashOperations<String, String, String> ops = redisTemplate.boundHashOps(SKUKILL_CACHE_PREFIX);
                    List<String> list = ops.multiGet(sessionSkuIds);
                    if (list != null) {
                        return list.stream()
                                .map(item -> JSON.parseObject(item, SeckillSkuRedisTo.class))
                                .toList();
                    }
                    break;
                }
            }
        }
        return null;
    }

    /**
     * 获取某个商品的秒杀预告信息
     *
     * @param skuId
     * @return
     */
    @Override
    public SeckillSkuRedisTo getSkuSeckillInfo(Long skuId) {
        //1.找到所有需要参与秒杀的key
        BoundHashOperations<String, String, String> ops = redisTemplate.boundHashOps(SKUKILL_CACHE_PREFIX);
        Set<String> keys = ops.keys();
        if (!CollectionUtils.isEmpty(keys)) {
            /**
             *
             \\d_ 表示\d_匹配一个数字,不好
             String reg = "\\d_" + skuId;
             if (Pattern.matches(reg, key)) {
             *
             * */
            for (String key : keys) {
                if (key.endsWith("_" + skuId)) {
                    String json = ops.get(key);
                    SeckillSkuRedisTo seckillSkuRedisTo = JSON.parseObject(json, SeckillSkuRedisTo.class);
                    //处理随机码
                    long startTime = seckillSkuRedisTo.getStartTime();
                    long endTime = seckillSkuRedisTo.getEndTime();
                    long curTime = new Date().getTime();
                    if (curTime >= startTime && curTime <= endTime) {
                        // 在秒杀活动时
                    } else {
                        // 不在秒杀活动时不应该传递随机码
                        seckillSkuRedisTo.setRandomCode("");
                    }
                    return seckillSkuRedisTo;
                }
            }
        }
        return null;
    }

    @Override
    public String kill(String killId, String key, Integer num) {
        //1.获取当前秒杀商品的详细信息
        BoundHashOperations<String, String, String> ops = redisTemplate.boundHashOps(SKUKILL_CACHE_PREFIX);
        String json = ops.get(killId);
        if (StringUtils.isEmpty(json)) {
            return null;
        }
        SeckillSkuRedisTo to = JSON.parseObject(json, SeckillSkuRedisTo.class);
        //2.校验合法性
        //2.1校验时间
        Long startTime = to.getStartTime();
        Long endTime = to.getEndTime();
        long nowTime = new Date().getTime();
        if (nowTime <= endTime && nowTime >= startTime) {
            //2.2校验随机码和商品id
            String randomCode = to.getRandomCode();
            String skuId = to.getPromotionSessionId() + "_" + to.getSkuId();
            if (randomCode.equals(key) && killId.equals(skuId)) {
                //2.3验证购物数量是否合理
                BigDecimal seckillLimit = to.getSeckillLimit();
                if (num <= seckillLimit.intValue()) {
                    //2.4验证这个人是否已经买过了。幂等性。
                    //如果秒杀成功，就去redis占位。 key:userId_sessionId_skuId;
                    MemberTo member = LoginUserInterceptor.loginUser.get();
                    String userKey = member.getId() + "_" + to.getPromotionSessionId() + "_" + skuId;
                    long ttl = endTime - nowTime;
                    Boolean b = redisTemplate.opsForValue().setIfAbsent(userKey, num.toString(), ttl, TimeUnit.MILLISECONDS);
                    if (Boolean.TRUE.equals(b)) {
                        //如果占位成功说明没有秒杀过
                        //2.5获取信号量
                        RSemaphore semaphore = redissonClient.getSemaphore(SeckillConstant.SKU_STOCK_SEMAPHORE + randomCode);
                        try {
                            boolean b1 = semaphore.tryAcquire(num, 100, TimeUnit.SECONDS);
                            if (b1) {
                                //add into MQ
                                String orderSn = IdWorker.getTimeId();
                                SeckillOrderTo seckillOrder = new SeckillOrderTo();
                                seckillOrder.setOrderSn(orderSn);
                                seckillOrder.setMemberId(member.getId());
                                seckillOrder.setNum(num);
                                seckillOrder.setPromotionSessionId(to.getPromotionSessionId());
                                seckillOrder.setSeckillPrice(to.getSeckillPrice());
                                seckillOrder.setSkuId(to.getSkuId());
                                rabbitTemplate.convertAndSend("order-event-exchange"
                                        , "order.seckill.order"
                                        , seckillOrder);
                                return orderSn;
                            }
                            return null;
                        } catch (InterruptedException e) {
                            return null;
                        }
                    } else {
                        return null;
                    }
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * 缓存活动信息
     * redis key start end time, value event list
     *
     * @param sessions
     */
    private void saveSessionInfos(List<SeckillSessionVo> sessions) {
        sessions.forEach(session -> {
            //long类型为时间戳
            long startTime = session.getStartTime().getTime();
            long endTime = session.getEndTime().getTime();
            String key = SeckillConstant.SESSIONS_CACHE_PREFIX + startTime + "_" + endTime;
            Boolean hasKey = redisTemplate.hasKey(key);
            if (Boolean.FALSE.equals(hasKey)) {
                List<String> sessionSkuIds = session.getRelationSkus().stream()
                        .map(relation -> relation.getPromotionSessionId() + "_" + relation.getSkuId())
                        .toList();
                redisTemplate.opsForList().leftPushAll(key, sessionSkuIds);
            }
        });
    }

    private void saveSessionSkuInfos(List<SeckillSessionVo> sessions) {
        sessions.forEach(session -> {
            BoundHashOperations<String, Object, Object> ops = redisTemplate.boundHashOps(SKUKILL_CACHE_PREFIX);
            List<SeckillSkuRelationVo> relations = session.getRelationSkus();
            relations.forEach(relation -> {
                String randomCode = UUID.randomUUID().toString().replace("-", "");
                String key = relation.getPromotionSessionId() + "_" + relation.getSkuId();
                Boolean hasKey = ops.hasKey(key);
                if (Boolean.FALSE.equals(hasKey)) {
                    SeckillSkuRedisTo seckillSkuRedisTo = new SeckillSkuRedisTo();
                    SkuInfoVo skuInfoVo = productFeignService.infoBySkuId(relation.getSkuId());

                    BeanUtils.copyProperties(relation, seckillSkuRedisTo);
                    //封装活动开始和结束时间
                    seckillSkuRedisTo.setStartTime(session.getStartTime().getTime());
                    seckillSkuRedisTo.setEndTime(session.getEndTime().getTime());
                    //封装sku基本信息
                    seckillSkuRedisTo.setSkuInfoVo(skuInfoVo);
                    //封装商品的随机码
                    seckillSkuRedisTo.setRandomCode(randomCode);
                    ops.put(key, JSON.toJSONString(seckillSkuRedisTo));
                    //如果当前这个场次的商品的库存信息已经上架就不需要上架了
                    //设置秒杀商品分布式信息量作为库存扣减信息
                    RSemaphore semaphore = redissonClient.getSemaphore(SeckillConstant.SKU_STOCK_SEMAPHORE + randomCode);
                    semaphore.trySetPermits(relation.getSeckillCount().intValue());
                }
            });
        });
    }
}
