package com.hychen11.order.service.impl;

import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.hychen11.common.constant.OrderConstant;
import com.hychen11.common.exception.NoStockException;
import com.hychen11.common.to.MemberTo;
import com.hychen11.common.to.OrderTo;
import com.hychen11.common.utils.R;
import com.hychen11.order.dao.PaymentInfoDao;
import com.hychen11.order.entity.OrderItemEntity;
import com.hychen11.order.entity.PaymentInfoEntity;
import com.hychen11.order.enume.OrderStatusEnum;
import com.hychen11.order.feign.CartFeignService;
import com.hychen11.order.feign.MemberFeignService;
import com.hychen11.order.feign.ProductFeignService;
import com.hychen11.order.feign.WareFeignService;
import com.hychen11.order.vo.*;
import com.hychen11.order.to.OrderCreateTo;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hychen11.common.utils.PageUtils;
import com.hychen11.common.utils.Query;

import com.hychen11.order.dao.OrderDao;
import com.hychen11.order.entity.OrderEntity;
import com.hychen11.order.service.OrderService;

import com.hychen11.order.interceptor.LoginUserInterceptor;
import com.hychen11.order.service.OrderItemService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;


@Service("orderService")
public class OrderServiceImpl extends ServiceImpl<OrderDao, OrderEntity> implements OrderService {

    private ThreadLocal<OrderSubmitVo> submitThreadLocal = new ThreadLocal<>();

    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private ThreadPoolExecutor executor;
    @Autowired
    private MemberFeignService memberFeignService;
    @Autowired
    private CartFeignService cartFeignService;
    @Autowired
    private WareFeignService wareFeignService;
    @Autowired
    private ProductFeignService productFeignService;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private PaymentInfoDao paymentInfoDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<OrderEntity> page = this.page(
                new Query<OrderEntity>().getPage(params),
                new QueryWrapper<OrderEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPageWithItem(Map<String, Object> params) {
        MemberTo member = LoginUserInterceptor.loginUser.get();
        Long memberId = member.getId();
        IPage<OrderEntity> page = this.page(
                new Query<OrderEntity>().getPage(params),
                new LambdaQueryWrapper<OrderEntity>()
                        .eq(OrderEntity::getMemberId, memberId).orderByDesc(OrderEntity::getId));
        if (page != null) {
            List<OrderEntity> orderList = page.getRecords().stream().map(order -> {
                List<OrderItemEntity> orderItems = orderItemService.list(new LambdaQueryWrapper<OrderItemEntity>()
                        .eq(OrderItemEntity::getOrderSn, order.getOrderSn()));
                order.setOrderItems(orderItems);
                return order;
            }).toList();
            page.setRecords(orderList);
            return new PageUtils(page);
        }
        return null;
    }

    @Override
    public OrderEntity getOrderByOrderSn(String orderSn) {
        return orderDao.selectOne(new LambdaQueryWrapper<OrderEntity>()
                .eq(OrderEntity::getOrderSn, orderSn));
    }

    /***
     * 获取订单确认页信息
     * 1、远程查询所有的地址列表
     * 2、远程查询购物车所有选中的购物项
     * 3、查询用户积分
     * 4、其他数据自动计算
     * 5、防重令牌
     *
     * */
    @Override
    public OrderConfirmVo confirmOrder() throws ExecutionException, InterruptedException {
        MemberTo member = LoginUserInterceptor.loginUser.get();
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        OrderConfirmVo orderConfirm = new OrderConfirmVo();
        CompletableFuture<Void> addressFuture = CompletableFuture.runAsync(() -> {
            //1.远程查询当前会员的地址
            RequestContextHolder.setRequestAttributes(requestAttributes);
            List<MemberAddressVo> address = memberFeignService.getAddress(member.getId());
            orderConfirm.setAddress(address);
        }, executor);
        CompletableFuture<Void> orderItemFuture = CompletableFuture.runAsync(() -> {
            //2.远程查询当前会员的购物车信息
            RequestContextHolder.setRequestAttributes(requestAttributes);
            List<OrderItemVo> orderItems = cartFeignService.getCurrentUserCartItems();
            orderConfirm.setItems(orderItems);
            //feign在远程调用之前要构造请求，会调用很多拦截器
            //RequestInterceptor interceptor : requestInterceptors
        }, executor).thenRunAsync(() -> {
            //查询库存信息
            List<OrderItemVo> items = orderConfirm.getItems();
            List<Long> skuIds = items.stream().map(OrderItemVo::getSkuId).toList();
            List<SkuHasStockVo> skusHasStock = wareFeignService.getSkusHasStock(skuIds);
            if (!CollectionUtils.isEmpty(skusHasStock)) {
                Map<Long, Boolean> stocks = skusHasStock.stream().collect(Collectors.toMap(SkuHasStockVo::getSkuId, SkuHasStockVo::getHasStock));
                orderConfirm.setStocks(stocks);
            }
        }, executor);
        //3.查询用户积分
        Integer integration = member.getIntegration();
        orderConfirm.setIntegration(integration);
        //4.价格等信息在实体类中自动计算
        //TODO：5.防重令牌
        String token = UUID.randomUUID().toString().replace("-", "");
        orderConfirm.setOrderToken(token);
        redisTemplate.opsForValue().set(OrderConstant.USER_ORDER_TOKEN_PREFIX + member.getId(), token, 30, TimeUnit.MINUTES);
        CompletableFuture.allOf(addressFuture, orderItemFuture).get();
        return orderConfirm;
    }

    /**
     * 下单
     *
     * @param orderSubmitVo
     * @return
     */

//        case 1: "令牌校验失败";
//        case 2: "锁失败";
//        case 3: "金额对比失败";
//        case 4: "验证令牌为空";
    @GlobalTransactional
    @Transactional
    @Override
    public SubmitOrderResVo submitOrder(OrderSubmitVo orderSubmitVo) {
        SubmitOrderResVo res = new SubmitOrderResVo();
        res.setCode(0);
        //从拦截器中拿到当前的用户
        MemberTo memberTo = LoginUserInterceptor.loginUser.get();
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        String orderToken = orderSubmitVo.getOrderToken();
        //1 原子验证令牌和删除令牌
        Long result = redisTemplate.execute(
                new DefaultRedisScript<Long>(script, Long.class),
                Arrays.asList(OrderConstant.USER_ORDER_TOKEN_PREFIX + memberTo.getId()),
                orderToken
        );

        if (result == 0L) {
            //token验证失败
            res.setCode(1);
            return res;
        }
        //2、创建订单、订单项等信息
        OrderCreateTo order = createOrder();
        // 3、验价
        BigDecimal payAmount = order.getOrder().getPayAmount();
        BigDecimal payPrice = orderSubmitVo.getPayPrice();
        if (Math.abs(payAmount.subtract(payPrice).doubleValue()) < 0.01) {
            //4、保存订单;
            saveOrder(order);
            // 5、库存锁定，只要有异常回滚订单数据
            // 订单号，所有订单项(skuId,skuName,num)
            WareSkuLockVo lockVo = new WareSkuLockVo();
            lockVo.setOrderSn(order.getOrder().getOrderSn());
            List<OrderItemVo> locks = order.getOrderItem().stream().map(item -> {
                OrderItemVo itemVo = new OrderItemVo();
                itemVo.setSkuId(item.getSkuId());
                itemVo.setCount(item.getSkuQuantity());
                itemVo.setTitle(item.getSkuName());
                return itemVo;
            }).collect(Collectors.toList());
            lockVo.setLocks(locks);

            //6 锁库存
            R r = wareFeignService.orderLockStock(lockVo);
            if (r.getCode() == 0) {
                res.setOrderEntity(order.getOrder());
                rabbitTemplate.convertAndSend("order-event-exchange", "order.create.order", order.getOrder());
                return res;
            } else {
                throw new NoStockException((String) r.get("msg"));
            }
        }
        res.setCode(3);
        return res;

    }


    /**
     * 保存订单数据
     *
     * @param order
     */
    private void saveOrder(OrderCreateTo order) {
        OrderEntity orderEntity = order.getOrder();
        List<OrderItemEntity> orderItems = order.getOrderItem();
        orderEntity.setModifyTime(new Date());
        orderDao.insert(orderEntity);
        orderItemService.saveBatch(orderItems);
    }

    /**
     * 创建订单方法
     *
     * @return
     */

    private OrderCreateTo createOrder() {
        OrderCreateTo orderCreateTo = new OrderCreateTo();
        //1.构建订单
        //生成订单号
        String orderSn = IdWorker.getTimeId();
        OrderEntity order = buildOrder(orderSn);
        orderCreateTo.setOrder(order);
        //2.获取到所有的订单项目
        List<OrderItemEntity> orderItems = buildOrderItems(orderSn);
        orderCreateTo.setOrderItem(orderItems);
        //3.计算价格积分等相关信息
        computePrice(order, orderItems);
        orderCreateTo.setPayPrice(order.getPayAmount());
        orderCreateTo.setFare(order.getFreightAmount());
        return orderCreateTo;
    }

    /**
     * 计算价格积分等相关信息
     *
     * @param order
     * @param orderItems
     */
    private void computePrice(OrderEntity order, List<OrderItemEntity> orderItems) {
        //1.订单价格相关计算
        //订单总额
        BigDecimal totalAmount = new BigDecimal("0.0");
        BigDecimal couponAmount = new BigDecimal("0.0");
        BigDecimal integrationAmount = new BigDecimal("0.0");
        BigDecimal promotionAmount = new BigDecimal("0.0");
        BigDecimal giftIntegration = new BigDecimal("0.0");
        BigDecimal giftGrowth = new BigDecimal("0.0");
        for (OrderItemEntity orderItem : orderItems) {
            BigDecimal realAmount = orderItem.getRealAmount();
            totalAmount = totalAmount.add(realAmount);
            couponAmount = couponAmount.add(orderItem.getCouponAmount());
            integrationAmount = integrationAmount.add(orderItem.getIntegrationAmount());
            promotionAmount = promotionAmount.add(orderItem.getPromotionAmount());
            giftIntegration = giftIntegration.add(new BigDecimal(orderItem.getGiftIntegration().toString()));
            giftGrowth = giftGrowth.add(new BigDecimal(orderItem.getGiftGrowth().toString()));
        }
        order.setTotalAmount(totalAmount);
        order.setPromotionAmount(promotionAmount);
        order.setCouponAmount(couponAmount);
        order.setIntegrationAmount(integrationAmount);
        //应付总额
        order.setPayAmount(totalAmount.add(order.getFreightAmount()));
        //2.设置积分等信息
        order.setIntegration(giftIntegration.intValue());
        order.setGrowth(giftGrowth.intValue());
        order.setDeleteStatus(0);//未删除
    }

    /**
     * 构建订单
     *
     * @param orderSn
     * @return
     */

    private OrderEntity buildOrder(String orderSn) {
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setOrderSn(orderSn);
        //1.设置会员信息
        MemberTo member = LoginUserInterceptor.loginUser.get();
        orderEntity.setMemberId(member.getId());
        orderEntity.setMemberUsername(member.getUsername());
        //2.获取收货地址信息
        OrderSubmitVo orderSubmitVo = submitThreadLocal.get();
        R r = wareFeignService.getFare(orderSubmitVo.getAddrId());
        FareVo fareVo = r.getData(new TypeReference<FareVo>() {
        });
        //设置运费信息
        orderEntity.setFreightAmount(fareVo.getFare());
        //设置收货人信息
        orderEntity.setReceiverProvince(fareVo.getAddress().getProvince());
        orderEntity.setReceiverCity(fareVo.getAddress().getCity());
        orderEntity.setReceiverDetailAddress(fareVo.getAddress().getDetailAddress());
        orderEntity.setReceiverName(fareVo.getAddress().getName());
        orderEntity.setReceiverPhone(fareVo.getAddress().getPhone());
        orderEntity.setReceiverPostCode(fareVo.getAddress().getPostCode());
        orderEntity.setReceiverRegion(fareVo.getAddress().getRegion());
        //设置订单状态
        orderEntity.setStatus(OrderStatusEnum.CREATE_NEW.getCode());
        orderEntity.setAutoConfirmDay(7);
        return orderEntity;
    }

    /**
     * 构建所有订单项
     *
     * @return
     */
    private List<OrderItemEntity> buildOrderItems(String orderSn) {
        List<OrderItemVo> currentUserCartItems = cartFeignService.getCurrentUserCartItems();
        if (!CollectionUtils.isEmpty(currentUserCartItems)) {
            //构建所有订单项
            return currentUserCartItems.stream().map(item -> {
                //构建一个订单项
                OrderItemEntity orderItemEntity = buildOrderItem(item);
                orderItemEntity.setOrderSn(orderSn);
                return orderItemEntity;
            }).toList();
        }
        return null;
    }

    /**
     * 构建一个订单项目
     *
     * @param orderItem
     * @return
     */
    private OrderItemEntity buildOrderItem(OrderItemVo orderItem) {
        OrderItemEntity orderItemEntity = new OrderItemEntity();
        //spu
        Long skuId = orderItem.getSkuId();
        SpuInfoVo spuInfo = productFeignService.getSpuInfoBySkuId(skuId);
        orderItemEntity.setSpuId(spuInfo.getId());
        orderItemEntity.setSpuName(spuInfo.getSpuName());
        orderItemEntity.setSpuPic(spuInfo.getSpuDescription());
        String brandName = productFeignService.getBrandNameById(spuInfo.getBrandId());
        orderItemEntity.setSpuBrand(brandName);
        orderItemEntity.setCategoryId(spuInfo.getCatalogId());
        //sku
        orderItemEntity.setSkuId(orderItem.getSkuId());
        orderItemEntity.setSkuName(orderItem.getTitle());
        orderItemEntity.setSkuPic(orderItem.getImage());
        orderItemEntity.setSkuPrice(orderItem.getPrice());
        String skuAttr = StringUtils.collectionToDelimitedString(orderItem.getSkuAttr(), ";");
        orderItemEntity.setSkuAttrsVals(skuAttr);
        orderItemEntity.setSkuQuantity(orderItem.getCount());
        //优惠信息[不做]
        //积分信息
        orderItemEntity.setGiftGrowth(orderItem.getPrice().multiply(new BigDecimal(orderItem.getCount().toString())).intValue());
        orderItemEntity.setGiftIntegration(orderItem.getPrice().multiply(new BigDecimal(orderItem.getCount().toString())).intValue());
        //订单项目价格信息
        orderItemEntity.setPromotionAmount(new BigDecimal("0.0"));
        orderItemEntity.setCouponAmount(new BigDecimal("0.0"));
        orderItemEntity.setIntegrationAmount(new BigDecimal("0.0"));
        BigDecimal orignPrice = orderItemEntity.getSkuPrice().multiply(new BigDecimal(orderItemEntity.getSkuQuantity().toString()));
        BigDecimal realAmount = orignPrice.subtract(orderItemEntity.getCouponAmount())
                .subtract(orderItemEntity.getIntegrationAmount())
                .subtract(orderItemEntity.getPromotionAmount());
        orderItemEntity.setRealAmount(realAmount);
        return orderItemEntity;
    }

    /**
     * 关闭订单功能
     *
     * @param order
     */
    @Override
    public void orderClose(OrderEntity order) {
        //函数调用场景：队列超过30min仍然是待付款，关闭订单
        //先查询当前订单的最新状态
        String orderSn = order.getOrderSn();
        OrderEntity dbOrder = orderDao.selectOne(new LambdaQueryWrapper<OrderEntity>()
                .eq(OrderEntity::getOrderSn, orderSn));
        if (!Objects.isNull(dbOrder) && dbOrder.getStatus() == OrderStatusEnum.CREATE_NEW.getCode()) {
            OrderEntity updateOrder = new OrderEntity();
            updateOrder.setStatus(OrderStatusEnum.CANCLED.getCode());
            updateOrder.setId(dbOrder.getId());
            orderDao.updateById(updateOrder);

            //关闭订单后再给解锁库存发个消息
            OrderTo orderTo = new OrderTo();
            BeanUtils.copyProperties(order, orderTo);
            try {
                //TODO: 保证消息一定会发送出去，每一个消息做好日志记录(给数据库保存每一个消息的详细信息)
                //TODO: 定期扫描数据库将失败的消息重新发送
                rabbitTemplate.convertAndSend("order-event-exchange", "order.release.other", orderTo);
            } catch (Exception e) {
                //TODO: 将没发送成功的消息进行重试发送
            }
        }

        rabbitTemplate.convertAndSend("order-event-exchange", "order.close.order", order);
    }

    /**
     * 获取当前订单的支付信息
     *
     * @param orderSn
     * @return
     */
    @Override
    public PayVo getOrderPay(String orderSn) {
        OrderEntity orderEntity = orderDao.selectOne(new LambdaQueryWrapper<OrderEntity>()
                .eq(OrderEntity::getOrderSn, orderSn));
        PayVo payVo = new PayVo();
        BigDecimal payAmount = orderEntity.getPayAmount().setScale(2, RoundingMode.UP);
        payVo.setTotal_amount(payAmount.toString());
        payVo.setOut_trade_no(orderEntity.getOrderSn());
        List<OrderItemEntity> orderItems = orderItemService.list(new LambdaQueryWrapper<OrderItemEntity>()
                .eq(OrderItemEntity::getOrderSn, orderSn));
        OrderItemEntity orderItem = orderItems.get(0);
        payVo.setSubject(orderItem.getSkuName());
        payVo.setBody(orderItem.getSkuAttrsVals());
        return payVo;
    }

    /**
     * 处理支付宝支付结果
     *
     * @param vo
     * @return
     */
    @Override
    public String handlePayResult(PayAsyncVo vo) {
        //1.保存交易流水
        PaymentInfoEntity paymentInfo = new PaymentInfoEntity();
        paymentInfo.setAlipayTradeNo(vo.getTrade_no());
        paymentInfo.setOrderSn(vo.getOut_trade_no());
        paymentInfo.setPaymentStatus(vo.getTrade_status());
        paymentInfo.setCallbackTime(vo.getNotify_time());
        paymentInfoDao.insert(paymentInfo);
        //2.修改订单状态信息
        String tradeStatus = vo.getTrade_status();
        if (tradeStatus.equals("TRADE_SUCCESS") || tradeStatus.equals("TRADE_FINISHED")) {
            String orderSn = vo.getOut_trade_no();
            orderDao.updateOrderStatus(orderSn, OrderStatusEnum.PAYED.getCode());
            return "success";
        }
        return "false";
    }
}