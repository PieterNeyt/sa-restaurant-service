package be.kdg.sa.restaurantservice.infrastructure.restaurant.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQTopology {
    public static final String EXCHANGE_NAME = "orders-topic-exchange";
    public static final String RESTAURANT_QUEUE = "restaurant.orders.queue";
    public static final String ORDER_RESPONSE_QUEUE = "order.response.queue";

    @Bean
    TopicExchange ordersExchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    Queue restaurantQueue() {
        return QueueBuilder.durable(RESTAURANT_QUEUE).build();
    }

    @Bean
    Queue orderResponseQueue() {
        return QueueBuilder.durable(ORDER_RESPONSE_QUEUE)
                .withArgument("x-message-ttl", 300_000)
                .build();
    }

    @Bean
    Binding restaurantQueueBinding() {
        return BindingBuilder.bind(restaurantQueue())
                .to(ordersExchange())
                .with("order.placed");
    }

    @Bean
    Binding orderResponseQueueBinding() {
        return BindingBuilder.bind(orderResponseQueue())
                .to(ordersExchange())
                .with("order.response.*");
    }
}
