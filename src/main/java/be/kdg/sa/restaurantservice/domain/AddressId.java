package be.kdg.sa.restaurantservice.domain;

import org.jmolecules.ddd.annotation.ValueObject;
import org.springframework.util.Assert;

import java.util.UUID;

@ValueObject
public record AddressId(UUID value) {
    public AddressId {
        Assert.notNull(value, "id cannot be null");
    }
    public static AddressId create() {
        return new AddressId(UUID.randomUUID());
    }
}
