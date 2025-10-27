package be.kdg.sa.restaurantservice.infrastructure.order.jpa;

import be.kdg.sa.restaurantservice.domain.order.OrderDish;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Embeddable
@Getter
public class JpaOrderDishEntity {
    @Column(nullable = false)
    private UUID dishId;

    @Column(nullable = false)
    private int amount;

    public JpaOrderDishEntity() {}

    public JpaOrderDishEntity(UUID dishId, int amount) {
        this.dishId = dishId;
        this.amount = amount;
    }

    public static List<JpaOrderDishEntity> from(List<OrderDish> dishes) {
        return dishes.stream()
                .map(d -> new JpaOrderDishEntity(d.dishId(),d.amount()))
                .toList();
    }

    public static List<OrderDish> toDomain(List<JpaOrderDishEntity> dishes) {
        return dishes.stream().map(d -> new OrderDish(d.amount,d.dishId)).toList();
    }
}
