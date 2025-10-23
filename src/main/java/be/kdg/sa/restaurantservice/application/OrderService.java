package be.kdg.sa.restaurantservice.application;

import be.kdg.sa.restaurantservice.application.command.RestaurantResponse;
import be.kdg.sa.restaurantservice.domain.order.IOrderMessageService;
import be.kdg.sa.restaurantservice.domain.order.Order;
import be.kdg.sa.restaurantservice.domain.order.OrderId;
import be.kdg.sa.restaurantservice.domain.order.OrderRepository;
import be.kdg.sa.restaurantservice.domain.restaurant.RestaurantId;
import be.kdg.sa.restaurantservice.domain.restaurant.RestaurantRepository;
import be.kdg.sa.restaurantservice.infrastructure.config.RabbitMQTopology;
import be.kdg.sa.restaurantservice.infrastructure.handler.OrderMessage;
import jakarta.transaction.Transactional;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class OrderService {
    private final OrderRepository orderRepository;
    private final IOrderMessageService orderMessageService;
    public OrderService(OrderRepository orderRepository, RabbitTemplate rabbitTemplate, IOrderMessageService orderMessageService) {
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
                .orElseThrow();

        order.checkRestaurant(restaurantId);
        order.deny(message);

        orderMessageService.sendDenyResponse(new RestaurantResponse(order.getOrderId().id(),order.getStatus().toString(), order.getMessage()));



        orderRepository.delete(order);
    }

    public void acceptOrder(UUID orderId,UUID restaurantId) {
        Order order = orderRepository.findByid(orderId)
                .orElseThrow();

        order.checkRestaurant(restaurantId);
        order.accept();

        orderMessageService.sendAcceptResponse(new RestaurantResponse(order.getOrderId().id(),order.getStatus().toString(), order.getMessage()));
        orderRepository.save(order);

    }

    public void orderIsReady(UUID orderId,UUID restaurantId) {
        Order order = orderRepository.findByid(orderId)
                .orElseThrow();

        order.checkRestaurant(restaurantId);
        order.ready();

        orderMessageService.sendReadyResponse(new RestaurantResponse(order.getOrderId().id(),order.getStatus().toString(), order.getMessage()));
        orderRepository.save(order);
    }
}
