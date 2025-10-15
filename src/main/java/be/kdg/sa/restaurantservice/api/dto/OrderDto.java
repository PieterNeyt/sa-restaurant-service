package be.kdg.sa.restaurantservice.api.dto;


import be.kdg.sa.restaurantservice.domain.order.Order;
import be.kdg.sa.restaurantservice.domain.order.OrderStatus;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

public record OrderDto(
        UUID orderId,
        UUID restaurantId,
        BigDecimal price,
        Date startDate,
        String message,
        OrderStatus accepted
) {
    public static OrderDto fromDomain(Order order) {
        return new OrderDto(
                order.getOrderId().id(),
                order.getRestaurantId().id(),
                order.getPrice(),
                order.getStartDate(),
                order.getMessage(),
                order.getStatus()
        );
    }
}