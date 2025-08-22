package com.hychen11.order.listener;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import com.hychen11.order.entity.OrderEntity;
import com.hychen11.order.service.OrderService;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @author ：hychen11
 * @Description:
 * @ClassName: OrderCloseListener
 * @date ：2025/8/18 13:46
 */
@Service
@Slf4j
@RabbitListener(queues = "order.release.order.queue")
public class OrderCloseListener {
    @Resource
    private OrderService orderService;

    @RabbitHandler
    public void orderCloseListener(OrderEntity order, Message message, Channel channel) throws IOException {
        log.info("收到过期的订单信息：准备关闭订单：{}", order.getOrderSn());
        try {
            orderService.orderClose(order);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
        }

    }
}
