package com.hychen11.ware.service.impl;

import com.alibaba.nacos.common.utils.CollectionUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hychen11.common.exception.NoStockException;
import com.hychen11.common.to.OrderTo;
import com.hychen11.common.to.SkuHasStockTo;
import com.hychen11.common.to.SkuInfoTo;
import com.hychen11.common.to.mq.StockDetailTo;
import com.hychen11.common.to.mq.StockLockedTo;
import com.hychen11.ware.entity.WareOrderTaskDetailEntity;
import com.hychen11.ware.entity.WareOrderTaskEntity;
import com.hychen11.ware.feign.ProductFeign;
import com.hychen11.ware.service.WareOrderTaskDetailService;
import com.hychen11.ware.service.WareOrderTaskService;
import com.hychen11.ware.vo.OrderItemVo;
import com.hychen11.ware.vo.SkuWareHasStock;
import com.hychen11.ware.vo.WareSkuLockVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hychen11.common.utils.PageUtils;
import com.hychen11.common.utils.Query;

import com.hychen11.ware.dao.WareSkuDao;
import com.hychen11.ware.entity.WareSkuEntity;
import com.hychen11.ware.service.WareSkuService;
import org.springframework.transaction.annotation.Transactional;


@Service("wareSkuService")
public class WareSkuServiceImpl extends ServiceImpl<WareSkuDao, WareSkuEntity> implements WareSkuService {
    @Autowired
    private WareSkuDao wareSkuDao;
    @Autowired
    private ProductFeign productFeign;
    @Autowired
    private WareOrderTaskService wareOrderTaskService;
    @Autowired
    private WareOrderTaskDetailService wareOrderTaskDetailService;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<WareSkuEntity> page = this.page(
                new Query<WareSkuEntity>().getPage(params),
                new QueryWrapper<WareSkuEntity>()
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
        LambdaQueryWrapper<WareSkuEntity> wrapper = new LambdaQueryWrapper<>();
        String skuId = (String) params.get("skuId");
        String wareId = (String) params.get("wareId");
        if (!StringUtils.isEmpty(skuId)) {
            wrapper.eq(WareSkuEntity::getSkuId, skuId);
        }
        if (!StringUtils.isEmpty(wareId)) {
            wrapper.eq(WareSkuEntity::getWareId, wareId);
        }
        IPage<WareSkuEntity> page = this.page(
                new Query<WareSkuEntity>().getPage(params),
                wrapper
        );

        return new PageUtils(page);
    }

    @Transactional
    @Override
    public void addStock(WareSkuEntity wareSkuEntity) {
        //判断如果没有库存记录就是新增操作
        LambdaQueryWrapper<WareSkuEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(WareSkuEntity::getSkuId, wareSkuEntity.getSkuId());
        wrapper.eq(WareSkuEntity::getWareId, wareSkuEntity.getWareId());
        List<WareSkuEntity> wareSkuEntities = wareSkuDao.selectList(wrapper);
        if (!CollectionUtils.isEmpty(wareSkuEntities)) {
            wareSkuEntity.setStockLocked(0);
            //远程查询sku的名字
            try {
                SkuInfoTo skuInfoTo = productFeign.infoBySkuId(wareSkuEntity.getSkuId());
                if (skuInfoTo != null) {
                    wareSkuEntity.setSkuName(skuInfoTo.getSkuName());
                }
            } catch (Exception e) {
                log.error("完成采购添加库存功能出现异常:"+e.getMessage(),e);
            }
            wareSkuDao.insert(wareSkuEntity);
        } else {
            wareSkuDao.addStock(wareSkuEntity);
        }
    }

    /**
     * 查询sku是否有库存
     *
     * @param skuIds
     * @return
     */
    @Override
    public List<SkuHasStockTo> getSkusHasStock(List<Long> skuIds) {
        List<SkuHasStockTo> tos = skuIds.stream().map(skuId -> {
            SkuHasStockTo skuHasStockTo = new SkuHasStockTo();
            Long stock = wareSkuDao.getSkuStock(skuId);
            skuHasStockTo.setHasStock(stock == null ? false : stock > 0);
            skuHasStockTo.setSkuId(skuId);
            return skuHasStockTo;
        }).toList();
        return tos;
    }

    /**
     * feign远程调用，根据skuId查询是否有库存
     * @param skuId
     * @return
     */
    @Override
    public boolean hasStockBySkuId(Long skuId) {
        WareSkuEntity wareSkuEntity = wareSkuDao.selectOne(new LambdaQueryWrapper<WareSkuEntity>().eq(WareSkuEntity::getSkuId, skuId));
        return wareSkuEntity.getStock() - wareSkuEntity.getStockLocked() > 0;
    }

    /**
     * 锁库存
     * <p>
     * 库存解锁场景：
     * 1）、下单成功，订单过期没有支付被系统自动取消、被用户手动取消。
     * 2）、下订单成功，库存锁定成功，接下来业务失败，导致订单回滚
     * 之前锁定的库存要自动解锁。
     *
     * @param vo
     * @return
     */
    @Transactional
    @Override
    public Boolean orderLockStock(WareSkuLockVo vo) {
        /**
         * 保存库存工作单
         */
        //构建并保存库存锁定任务日志
        WareOrderTaskEntity taskEntity = new WareOrderTaskEntity();
        taskEntity.setOrderSn(vo.getOrderSn());
        wareOrderTaskService.save(taskEntity);

        // 找出所有有库存的ware
        List<SkuWareHasStock> skuWareHasStocks = queryWareIdsBySkuId(vo);

        for (SkuWareHasStock stock : skuWareHasStocks) {
            Long skuId = stock.getSkuId();
            List<Long> wareIds = stock.getWareIds();
            if (CollectionUtils.isEmpty(wareIds)) {
                throw new NoStockException(skuId);
            }
            boolean skuStocked = false;
            /**
             * 1.如果每一件商品都锁成功，将当前商品锁定了几件的工作单记录发送给MQ
             * 2.锁定失败，前面保存的工作单信息就回滚了。发送出去的消息，即使要解锁记录，但是去数据库查不到id,所以不用解锁
             */
            Integer skuNum = stock.getSkuNum();
            String skuName = stock.getSkuName();
            for (Long wareId : wareIds) {
                Long update = wareSkuDao.lockSkuStock(skuId, wareId, skuNum);
                if (update == 1) {
                    skuStocked = true;
                    // 锁定成功，保存库存最新快照
                    WareOrderTaskDetailEntity stockDetail = saveStockLockedSnapshot(taskEntity, skuId, skuName, skuNum, wareId);
                    // 发送锁定库存消息至延时队列
                    StockLockedTo stockLockedTo = new StockLockedTo();
                    stockLockedTo.setTaskId(taskEntity.getId());
                    StockDetailTo stockDetailTo = new StockDetailTo();
                    BeanUtils.copyProperties(stockDetail, stockDetailTo);
                    //防止回滚以后找不到数据
                    stockLockedTo.setTaskDetail(stockDetailTo);
                    rabbitTemplate.convertAndSend("stock-event-exchange", "stock.locked", stockLockedTo);
                    break;
                }
                // 仓库锁失败，尝试下一个仓库
            }
            if (!skuStocked) {
                //当前商品所有仓库都没有锁住
                throw new NoStockException(skuId);
            }
        }
        //全部上锁成功
        return true;
    }

    private WareOrderTaskDetailEntity saveStockLockedSnapshot(WareOrderTaskEntity task, Long skuId, String skuName, Integer num, Long wareId) {
        WareOrderTaskDetailEntity stockDetail = new WareOrderTaskDetailEntity();
        stockDetail.setSkuId(skuId);
        stockDetail.setSkuNum(num);
        stockDetail.setTaskId(task.getId());
        stockDetail.setWareId(wareId);
        stockDetail.setLockStatus(1);
        wareOrderTaskDetailService.save(stockDetail);
        return stockDetail;
    }

    private List<SkuWareHasStock> queryWareIdsBySkuId(WareSkuLockVo vo) {
        List<OrderItemVo> orderItems = vo.getOrderItems();
        return orderItems.stream().map(item -> {
            Long skuId = item.getSkuId();
            List<Long> wareIds = wareSkuDao.listWareHasStock(skuId);
            return SkuWareHasStock.builder()
                    .skuId(skuId)
                    .skuNum(item.getCount())
                    .skuName(item.getTitle())
                    .wareIds(wareIds)
                    .build();
        }).collect(Collectors.toList());
    }


    @Override
    public void unlockStock(OrderTo order) {
        String orderSn = order.getOrderSn();
        //查一下库存工作单的状态，防止重复解锁
        WareOrderTaskEntity taskEntity = wareOrderTaskService.getOrderTaskByOrderSn(orderSn);
        Long taskEntityId = taskEntity.getId();
        //按照工作单id找到没有解锁的库存，进行解锁
        List<WareOrderTaskDetailEntity> taskDetailEntities = wareOrderTaskDetailService
                .list(new LambdaQueryWrapper<WareOrderTaskDetailEntity>()
                        .eq(WareOrderTaskDetailEntity::getTaskId, taskEntityId)
                        .eq(WareOrderTaskDetailEntity::getLockStatus, 1));
        if (!CollectionUtils.isEmpty(taskDetailEntities)) {
            //解锁库存
            for (WareOrderTaskDetailEntity taskDetailEntity : taskDetailEntities) {
                unlockStock(taskDetailEntity);
            }
        }
    }

    @Transactional
    @Override
    public void unlockStock(WareOrderTaskDetailEntity taskDetailEntity) {
        Long skuId = taskDetailEntity.getSkuId();
        Long wareId = taskDetailEntity.getWareId();
        Integer skuNum = taskDetailEntity.getSkuNum();
        wareSkuDao.releaseLocked(skuId, wareId, skuNum);

        WareOrderTaskDetailEntity taskDetail = new WareOrderTaskDetailEntity();
        taskDetail.setId(taskDetailEntity.getId());
        taskDetail.setLockStatus(2);
        wareOrderTaskDetailService.updateById(taskDetail);
    }
}