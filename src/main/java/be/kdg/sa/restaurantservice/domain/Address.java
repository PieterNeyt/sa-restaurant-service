package be.kdg.sa.restaurantservice.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.jmolecules.ddd.annotation.AggregateRoot;

import java.util.UUID;

@AggregateRoot
@Getter
@ToString
public class Address {
    private final AddressId addressId;
    private final String street;
    private final String streetNumber;
    private final String city;
    private final String postalCode;
    private final String country;

    public Address(String city, String streetNumber, String street, String postalCode, String country) {
        this.addressId = AddressId.create();

        this.city = city;
        this.streetNumber = streetNumber;
        this.street = street;
        this.postalCode = postalCode;
        this.country = country;
    }
}