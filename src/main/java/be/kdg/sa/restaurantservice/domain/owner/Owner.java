package be.kdg.sa.restaurantservice.domain.owner;

import be.kdg.sa.restaurantservice.domain.address.Address;
import org.jmolecules.ddd.annotation.AggregateRoot;
import org.jmolecules.ddd.annotation.Identity;


@AggregateRoot
public class Owner {
    @Identity
    private final OwnerId id;
    private final String firstName;
    private final String lastName;
    private final String email;
    private Address address;

    public Owner(String firstName, String lastName, String email, Address address) {
        this.id = OwnerId.create();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.address = address;
    }
}
