package be.kdg.sa.restaurantservice.domain;

import org.jmolecules.ddd.annotation.ValueObject;
import org.springframework.util.Assert;

import java.util.UUID;

@ValueObject
public record RestaurantId(UUID value) {
    public RestaurantId {
        Assert.notNull(value, "id cannot be null");
    }
    public static RestaurantId create() {
        return new RestaurantId(UUID.randomUUID());
    }
}
