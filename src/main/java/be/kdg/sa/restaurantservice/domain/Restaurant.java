package be.kdg.sa.restaurantservice.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.jmolecules.ddd.annotation.AggregateRoot;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@AggregateRoot
public class Restaurant {
    private final RestaurantId id;
    private final OwnerId ownerId;

    private final Address address;
    private final RestaurantType type;
    private final String email;
    private final String logo;
    private final List<Dish> menu;

    public Restaurant(OwnerId ownerId, Address address, RestaurantType type, String email, String logo) {
        this.id = RestaurantId.create();
        this.ownerId = ownerId;
        this.address = address;
        this.type = type;
        this.email = email;
        this.logo = logo;
        this.menu = new ArrayList<>();
    }

}
