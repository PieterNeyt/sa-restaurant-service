package be.kdg.sa.restaurantservice.application;

import be.kdg.sa.restaurantservice.application.command.RestaurantResponse;
import be.kdg.sa.restaurantservice.domain.NotFoundException;
import be.kdg.sa.restaurantservice.domain.order.IOrderMessagePublisher;
import be.kdg.sa.restaurantservice.domain.order.Order;
import be.kdg.sa.restaurantservice.domain.order.OrderId;
import be.kdg.sa.restaurantservice.domain.order.OrderRepository;
import be.kdg.sa.restaurantservice.domain.restaurant.RestaurantId;
import be.kdg.sa.restaurantservice.infrastructure.handler.OrderMessage;
import jakarta.transaction.Transactional;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class OrderService {
    private final OrderRepository orderRepository;
    private final IOrderMessagePublisher orderMessageService;
    public OrderService(OrderRepository orderRepository, RabbitTemplate rabbitTemplate, IOrderMessagePublisher orderMessageService) {
        this.orderRepository = orderRepository;
        this.orderMessageService = orderMessageService;
    }

    public void processIncomingOrder(OrderMessage msg) {
        Order order = new Order(
                new OrderId(msg.id()),
                new RestaurantId(msg.restaurantId()),
                msg.totalPrice()
        );
        orderRepository.save(order);
    }

    public List<Order> getAllOrderFromRestaurant(UUID restaurantId) {
        return orderRepository.findOrdersByRestaurantId(restaurantId);
    }

    public void denyOrder(UUID orderId,UUID restaurantId, String message) {
        Order order = orderRepository.findByid(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found"));
        order.deny(message,restaurantId);

        orderMessageService.sendDenyResponse(new RestaurantResponse(order.getOrderId().id(),order.getStatus().toString(), order.getMessage()));



        orderRepository.delete(order);
    }

    public void acceptOrder(UUID orderId,UUID restaurantId) {
        Order order = orderRepository.findByid(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found"));
        order.accept(restaurantId);

        orderMessageService.sendAcceptResponse(new RestaurantResponse(order.getOrderId().id(),order.getStatus().toString(), order.getMessage()));
        orderRepository.save(order);
    }

    public void orderIsReady(UUID orderId,UUID restaurantId) {
        Order order = orderRepository.findByid(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found"));
        order.ready(restaurantId);

        orderMessageService.sendReadyResponse(new RestaurantResponse(order.getOrderId().id(),order.getStatus().toString(), order.getMessage()));
        orderRepository.save(order);
    }
}
