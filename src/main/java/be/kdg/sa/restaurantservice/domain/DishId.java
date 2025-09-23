package be.kdg.sa.restaurantservice.domain;

import org.jmolecules.ddd.annotation.ValueObject;
import org.springframework.util.Assert;

import java.util.UUID;

@ValueObject
public record DishId(UUID value) {
    public DishId {
        Assert.notNull(value, "id cannot be null");
    }
    public static DishId create() {
        return new DishId(UUID.randomUUID());
    }
}
