package be.kdg.sa.restaurantservice.infrastructure.jpa;

import be.kdg.sa.restaurantservice.domain.address.Address;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class JpaAddressEmbeddable {
    @Column(name = "address_street")
    private String street;

    @Column(name = "address_street_number")
    private String streetNumber;

    @Column(name = "address_city")
    private String city;

    @Column(name = "address_postal_code")
    private String postalCode;

    @Column(name = "address_country")
    private String country;

    public JpaAddressEmbeddable() {}

    public JpaAddressEmbeddable(String street, String streetNumber, String city,
                                String postalCode, String country) {
        this.street = street;
        this.streetNumber = streetNumber;
        this.city = city;
        this.postalCode = postalCode;
        this.country = country;
    }

    public static JpaAddressEmbeddable fromDomain(Address address) {
        if (address == null) return null;
        return new JpaAddressEmbeddable(
                address.getStreet(),
                address.getStreetNumber(),
                address.getCity(),
                address.getPostalCode(),
                address.getCountry()
        );
    }

    public Address toDomain() {
        if (street == null && city == null) return null;
        return new Address(city, streetNumber, street, postalCode, country);
    }
}