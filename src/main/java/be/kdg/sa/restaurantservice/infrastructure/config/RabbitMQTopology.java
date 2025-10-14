package be.kdg.sa.restaurantservice.infrastructure.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQTopology {
    public static final String ORDER_EXCHANGE_NAME = "order-exchange";
    public static final String ORDER_QUEUE_NAME = "order-queue";

    @Bean
    public TopicExchange orderExchange() {
        return new TopicExchange(ORDER_EXCHANGE_NAME);
    }

    @Bean
    public Queue orderQueue() {
        return QueueBuilder.nonDurable(ORDER_QUEUE_NAME).build();
    }

    @Bean
    public Binding orderQueueBinding() {
        return BindingBuilder.bind(orderQueue()).to(orderExchange()).with("order.place.*");
    }
}
