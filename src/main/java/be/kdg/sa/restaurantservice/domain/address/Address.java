package be.kdg.sa.restaurantservice.domain.address;

import lombok.Getter;
import lombok.ToString;
import org.jmolecules.ddd.annotation.Entity;


import lombok.EqualsAndHashCode; // Voeg deze import toe voor EqualsAndHashCode
import lombok.Getter;
import lombok.ToString;
import org.jmolecules.ddd.annotation.ValueObject; // Gebruik @ValueObject


@ValueObject
@Getter
@ToString
@EqualsAndHashCode
public class Address {

    private final String street;
    private final String streetNumber;
    private final String city;
    private final String postalCode;
    private final String country;

    public Address(String city, String streetNumber, String street, String postalCode, String country) {
        this.city = city;
        this.streetNumber = streetNumber;
        this.street = street;
        this.postalCode = postalCode;
        this.country = country;
    }
}