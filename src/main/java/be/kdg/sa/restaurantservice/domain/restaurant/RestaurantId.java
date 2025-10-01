package be.kdg.sa.restaurantservice.domain.restaurant;

import org.jmolecules.ddd.annotation.ValueObject;
import org.springframework.util.Assert;

import java.util.UUID;

@ValueObject
public record RestaurantId(UUID id) {
    public RestaurantId {
        Assert.notNull(id, "id cannot be null");
    }
    public static RestaurantId create() {
        return new RestaurantId(UUID.randomUUID());
    }
}
