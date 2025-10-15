package be.kdg.sa.restaurantservice.infrastructure.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQTopology {
    public static final String ORDER_QUEUE_NAME = "order-queue";

    public static final String RESPONSE_EXCHANGE_NAME = "restaurant-response-exchange";
    public static final String RESPONSE_QUEUE_NAME = "restaurant-response-queue";

    @Bean
    public TopicExchange responseExchange() {
        return new TopicExchange(RESPONSE_EXCHANGE_NAME);
    }

    @Bean
    public Queue responseQueue() {
        return QueueBuilder.nonDurable(RESPONSE_QUEUE_NAME).build();
    }

    @Bean
    public Binding responseQueueBinding() {
        return BindingBuilder.bind(responseQueue())
                .to(responseExchange())
                .with("order.response.*");
    }
}
