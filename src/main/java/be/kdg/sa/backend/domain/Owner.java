package be.kdg.sa.backend.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.UUID;
@Getter
@Setter
@ToString
public class Owner {
    public UUID ownerId;
    public String firstName;
    public String lastName;
    public String email;
    public String phoneNumber;
    public Address address;
    public Date birthDate;
    public Restaurant restaurant;

    public Owner(Address address,Restaurant restaurant, String phoneNumber, UUID ownerId, String lastName, String firstName, String email, Date birthDate) {
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.ownerId = ownerId;
        this.lastName = lastName;
        this.firstName = firstName;
        this.email = email;
        this.birthDate = birthDate;
        this.restaurant = restaurant;
    }
}
