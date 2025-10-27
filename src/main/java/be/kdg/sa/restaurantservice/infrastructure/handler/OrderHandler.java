package be.kdg.sa.restaurantservice.infrastructure.handler;

import be.kdg.sa.restaurantservice.application.OrderService;
import be.kdg.sa.restaurantservice.infrastructure.config.RabbitMQTopology;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OrderHandler {
    private final OrderService orderService;

    public OrderHandler(OrderService orderService) {
        this.orderService = orderService;
    }

    @RabbitListener(queues = "${rabbit.restaurant.order.queue}")
    public void receiveOrder(OrderMessage msg) {
        log.info("Received Order Message: {}", msg);
        orderService.processIncomingOrder(msg);
    }
}
