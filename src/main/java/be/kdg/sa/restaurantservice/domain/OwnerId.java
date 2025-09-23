package be.kdg.sa.restaurantservice.domain;

import org.jmolecules.ddd.annotation.ValueObject;
import org.springframework.util.Assert;

import java.util.UUID;

@ValueObject
public record OwnerId(UUID value) {
    public OwnerId {
        Assert.notNull(value, "id cannot be null");
    }
    public static OwnerId create() {
        return new OwnerId(UUID.randomUUID());
    }
}
