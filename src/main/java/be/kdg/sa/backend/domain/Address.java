package be.kdg.sa.backend.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Getter
@Setter
@ToString
public class Address {
    public UUID addressId;
    public String street;
    public String StreetNumber;
    public String city;
    public String postalCode;
    public String country;

    public Address(String city, String streetNumber, String street, String postalCode, String country) {
        this.city = city;
        StreetNumber = streetNumber;
        this.street = street;
        this.postalCode = postalCode;
        this.country = country;
    }
}
