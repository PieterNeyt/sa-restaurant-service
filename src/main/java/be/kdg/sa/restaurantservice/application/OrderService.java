package be.kdg.sa.restaurantservice.application;

import be.kdg.sa.restaurantservice.domain.order.Order;
import be.kdg.sa.restaurantservice.domain.order.OrderId;
import be.kdg.sa.restaurantservice.domain.order.OrderRepository;
import be.kdg.sa.restaurantservice.domain.restaurant.RestaurantId;
import be.kdg.sa.restaurantservice.infrastructure.handler.OrderMessage;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class OrderService {
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void processIncomingOrder(OrderMessage msg) {
        Order order = new Order(
                new OrderId(msg.id()),
                new RestaurantId(msg.restaurantId()),
                msg.totalPrice()
        );
        orderRepository.save(order);
    }
}
