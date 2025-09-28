package be.kdg.sa.restaurantservice.domain.Restaurant;

import org.jmolecules.ddd.annotation.ValueObject;
import org.springframework.util.Assert;

import java.util.UUID;

@ValueObject
public record ScheduledDishChangeId(UUID id) {
    public ScheduledDishChangeId {
        Assert.notNull(id, "id cannot be null");
    }
    public static ScheduledDishChangeId create() {
        return new ScheduledDishChangeId(UUID.randomUUID());
    }
}
