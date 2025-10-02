package be.kdg.sa.restaurantservice.domain.owner;

import org.jmolecules.ddd.annotation.ValueObject;
import org.springframework.util.Assert;

import java.util.UUID;

@ValueObject
public record OwnerId(UUID id) {
    public OwnerId {
        Assert.notNull(id, "id cannot be null");
    }
    public static OwnerId create() {
        return new OwnerId(UUID.randomUUID());
    }
}
