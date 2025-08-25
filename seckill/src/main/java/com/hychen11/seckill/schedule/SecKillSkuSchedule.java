package com.hychen11.seckill.schedule;

import com.hychen11.common.constant.SeckillConstant;
import com.hychen11.seckill.service.SecKillService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author ：hychen11
 * @Description:秒杀库存定时上架任务
 *  *      每天晚上3点，上架最近三天需要秒杀的商品
 *  *      当天00:00:00 - 23:59:59
 *  *      明天00:00:00 - 23:59:59
 *  *      后天00:00:00 - 23:59:59
 * @ClassName: SecKillSkuSchedule
 * @date ：2025/8/23 23:14
 */
@Service
@Slf4j
public class SecKillSkuSchedule {
    @Resource
    private SecKillService secKillService;

    @Resource
    private RedissonClient redissonClient;

    //TODO: 幂等性处理
    @Scheduled(cron = "0 0 3 * * ?")
    public void uploadSecKillLatest3Days(){
        //1.重复上架无需处理
        log.info("秒杀商品上架");
        // 分布式锁
        RLock lock = redissonClient.getLock(SeckillConstant.UPLOAD_LOCK);
        lock.lock(10, TimeUnit.SECONDS);
        try{
            secKillService.uploadSecKillLatest3Days();
        }finally {
            lock.unlock();
        }

    }
}
