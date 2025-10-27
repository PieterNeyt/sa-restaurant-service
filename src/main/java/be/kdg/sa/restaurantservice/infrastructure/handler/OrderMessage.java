package be.kdg.sa.restaurantservice.infrastructure.handler;

import be.kdg.sa.restaurantservice.domain.order.OrderDish;
import be.kdg.sa.restaurantservice.domain.restaurant.dish.DishId;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record OrderMessage(UUID id,
                           UUID restaurantId,
                           BigDecimal totalPrice,
                           List<DishMessage> dishes) {

    public record DishMessage(UUID dishId, int amount) {
        public static List<OrderDish> toDomain(List<DishMessage> dishes) {
            return dishes.stream()
                    .map(d -> new OrderDish(d.amount,d.dishId))
                    .toList();
        }
    }
}

