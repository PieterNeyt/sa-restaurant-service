package be.kdg.sa.backend.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@ToString
public class Restaurant {
    public UUID restaurantId;
    public Address address;
    public String email;
    public Owner owner;
    public RestaurantType restaurantType;
    public List<Dish> menu;
    public String logo;

    //TODO: openingsuren


    public Restaurant(RestaurantType restaurantType, Owner owner, List<Dish> menu, String logo, String email, Address address) {
        this.restaurantType = restaurantType;
        this.owner = owner;
        this.menu = menu;
        this.logo = logo;
        this.email = email;
        this.address = address;
        this.restaurantId = UUID.randomUUID();
    }
}
