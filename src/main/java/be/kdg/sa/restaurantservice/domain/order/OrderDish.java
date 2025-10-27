package be.kdg.sa.restaurantservice.domain.order;

import jakarta.persistence.Embeddable;

import java.util.UUID;

@Embeddable
public record OrderDish(int amount, UUID dishId) {
}
