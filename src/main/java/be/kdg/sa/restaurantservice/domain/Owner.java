package be.kdg.sa.restaurantservice.domain;

import org.jmolecules.ddd.annotation.AggregateRoot;


@AggregateRoot
public class Owner {
    private final OwnerId id;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final Address address;

    public Owner(String firstName, String lastName, String email, Address address) {
        this.id = OwnerId.create();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.address = address;
    }
}
