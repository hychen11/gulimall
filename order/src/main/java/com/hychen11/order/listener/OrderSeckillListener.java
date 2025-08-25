package com.hychen11.order.listener;

import com.hychen11.common.to.mq.SeckillOrderTo;
import com.hychen11.order.service.OrderService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @author ：hychen11
 * @Description:
 * @ClassName: OrderSeckillListener
 * @date ：2025/8/25 12:44
 */
@RabbitListener(queues = {"order.seckill.order.queue"})
@Slf4j
@Component
public class OrderSeckillListener {
    @Resource
    private OrderService orderService;
    @RabbitHandler
    public void listener(SeckillOrderTo seckillOrderTo, Message message, Channel channel) throws IOException {
        log.info("收到秒杀订单，正在处理。。。");
        try{
            orderService.createSeckillOrder(seckillOrderTo);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        }catch (Exception e){
            channel.basicReject(message.getMessageProperties().getDeliveryTag(),true);
        }
    }
}
