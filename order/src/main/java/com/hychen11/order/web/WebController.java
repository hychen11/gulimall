package com.hychen11.order.web;

import com.hychen11.order.entity.OrderEntity;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.UUID;

/**
 * @author ：hychen11
 * @Description:
 * @ClassName: WebController
 * @date ：2025/8/18 13:18
 */
@Controller
public class WebController {
    @Resource
    private RabbitTemplate rabbitTemplate;

    @ResponseBody
    @GetMapping("/test/createOrder")
    public String createOrderTest() {
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setOrderSn(UUID.randomUUID().toString());
        //给mq发送消息
        String key = "order.create.order";
        rabbitTemplate.convertAndSend("order-event-exchange", key, orderEntity);
        return "ok";
    }

    @GetMapping("/{page}.html")
    public String list(@PathVariable String page) {
        return page;
    }
}
