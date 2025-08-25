package com.hychen11.order.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ：hychen11
 * @Description:
 * @ClassName: RabbitMQCreateConfig
 * @date ：2025/8/18 13:36
 */
@Configuration
public class RabbitMQCreateConfig {
    @Bean
    public Queue orderDelayQueue() {
        /**
         * 设置
         * x-dead-letter-exchange
         * x-dead-letter-routing-key
         * x-message-ttl
         */
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange", "order-event-exchange");
        arguments.put("x-dead-letter-routing-key", "order.release.order");
        arguments.put("x-message-ttl", 60000);
        return new Queue("order.delay.queue", true, false, false, arguments);
    }

    @Bean
    public Queue orderReleaseOrderQueue() {
        return new Queue("order.release.order.queue", true, false, false, null);
    }

    @Bean
    public Exchange orderEventExchange() {
        return new TopicExchange("order-event-exchange", true, false, null);
    }

    @Bean
    public Binding orderCreateOrderBinding() {
        return new Binding("order.delay.queue", Binding.DestinationType.QUEUE
                , "order-event-exchange", "order.create.order", null);
    }

    @Bean
    public Binding orderReleaseOrderBinding() {
        return new Binding("order.release.order.queue", Binding.DestinationType.QUEUE
                , "order-event-exchange", "order.release.order", null);
    }

    /**
     * 订单关闭直接和库存释放进行绑定
     *
     * @return
     */
    @Bean
    public Binding OrderReleaseOtherBinding() {
        return new Binding("stock.release.stock.queue", Binding.DestinationType.QUEUE
                , "order-event-exchange", "order.release.other.#", null);
    }

    /**
     * 秒杀消息队列
     *
     * @return
     */
    @Bean
    public Queue orderSeckillOrderQueue() {
        return new Queue("order.seckill.order.queue", true, false, false, null);
    }

    @Bean
    public Binding orderSeckillOrderBinding() {
        return new Binding("order.seckill.order.queue", Binding.DestinationType.QUEUE
                , "order-event-exchange", "order.seckill.order", null);
    }
}
