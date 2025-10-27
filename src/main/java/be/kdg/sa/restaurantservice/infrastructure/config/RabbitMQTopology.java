package be.kdg.sa.restaurantservice.infrastructure.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQTopology {
    @Value("${rabbit.order.exchange}")
    public String ORDER_EXCHANGE_NAME;
    @Value("${rabbit.restaurant.response.exchange}")
    public String RESTAURANT_RESPONSE_EXCHANGE_NAME;
    @Value("${rabbit.delivery.exchange}")
    public String DELIVERY_EXCHANGE_NAME;
    @Value("${rabbit.restaurant.order.queue}")
    public String RESTAURANT_ORDER_QUEUE;

    @Value("${rabbit.order.routing.key}")
    public String RESTAURANT_ORDER_ROUTING_KEY;


    //ontvanget bestellingen
    @Bean
    Queue restaurantOrderQueue() {
        return QueueBuilder.nonDurable(RESTAURANT_ORDER_QUEUE).build();
    }

    //alles wat binnenkomt van order topic exange opvangen en routen naar de restaurant order que bij key van order.place
    @Bean
    Binding orderBinding(Queue restaurantOrderQueue) {
        return BindingBuilder.bind(restaurantOrderQueue)
                .to(new TopicExchange(ORDER_EXCHANGE_NAME))
                .with(RESTAURANT_ORDER_ROUTING_KEY);
    }

    @Bean
    TopicExchange restaurantResponseExchange() {
        return new TopicExchange(RESTAURANT_RESPONSE_EXCHANGE_NAME, true, false);
    }

    @Bean
    TopicExchange deliveryExchange() {
        return new TopicExchange(DELIVERY_EXCHANGE_NAME, true, false);
    }
}
