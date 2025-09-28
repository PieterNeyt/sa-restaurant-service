package be.kdg.sa.restaurantservice.domain.Owner;

import be.kdg.sa.restaurantservice.domain.Address.AddressId;
import org.jmolecules.ddd.annotation.AggregateRoot;


@AggregateRoot
public class Owner {
    private final OwnerId id;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final AddressId addressId;

    public Owner(String firstName, String lastName, String email, AddressId addressId) {
        this.id = OwnerId.create();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.addressId = addressId;
    }
}
