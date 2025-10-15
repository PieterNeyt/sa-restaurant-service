package be.kdg.sa.restaurantservice.infrastructure.handler;

import be.kdg.sa.restaurantservice.application.OrderService;
import be.kdg.sa.restaurantservice.infrastructure.config.RabbitMQTopology;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class OrderHandler {
    private final OrderService orderService;

    public OrderHandler(OrderService orderService) {
        this.orderService = orderService;
    }

    @RabbitListener(queues = RabbitMQTopology.ORDER_QUEUE_NAME)
    public void receiveOrder(OrderMessage msg) {
        orderService.processIncomingOrder(msg);
    }
}
