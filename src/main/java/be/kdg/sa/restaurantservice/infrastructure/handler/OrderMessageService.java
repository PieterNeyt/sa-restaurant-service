package be.kdg.sa.restaurantservice.infrastructure.handler;

import be.kdg.sa.restaurantservice.application.command.RestaurantResponse;
import be.kdg.sa.restaurantservice.domain.order.IOrderMessageService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class OrderMessageService implements IOrderMessageService {
    @Value("${rabbit.restaurant.response.exchange}")
    public String RESTAURANT_RESPONSE_EXCHANGE_NAME;

    private final RabbitTemplate rabbitTemplate;

    public OrderMessageService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void sendReadyResponse(RestaurantResponse restaurantResponse) {
        rabbitTemplate.convertAndSend( RESTAURANT_RESPONSE_EXCHANGE_NAME,
                "order.ready." + restaurantResponse.orderId(),restaurantResponse);
    }

    @Override
    public void sendAcceptResponse(RestaurantResponse restaurantResponse) {
        rabbitTemplate.convertAndSend( RESTAURANT_RESPONSE_EXCHANGE_NAME,
                "order.accept." + restaurantResponse.orderId(),restaurantResponse);
    }

    @Override
    public void sendDenyResponse(RestaurantResponse restaurantResponse) {
        rabbitTemplate.convertAndSend( RESTAURANT_RESPONSE_EXCHANGE_NAME,
                "order.deny." + restaurantResponse.orderId(),restaurantResponse);
    }
}
