package be.kdg.sa.restaurantservice.domain.address;

import org.jmolecules.ddd.annotation.ValueObject;
import org.springframework.util.Assert;

import java.util.UUID;

@ValueObject
public record AddressId(UUID id) {
    public AddressId {
        Assert.notNull(id, "id cannot be null");
    }
    public static AddressId create() {
        return new AddressId(UUID.randomUUID());
    }
}
