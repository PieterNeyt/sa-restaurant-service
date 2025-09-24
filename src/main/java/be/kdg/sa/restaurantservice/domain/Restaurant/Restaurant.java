package be.kdg.sa.restaurantservice.domain.Restaurant;

import be.kdg.sa.restaurantservice.domain.Address.Address;
import be.kdg.sa.restaurantservice.domain.Address.AddressId;
import be.kdg.sa.restaurantservice.domain.Owner.OwnerId;
import lombok.Getter;
import org.jmolecules.ddd.annotation.AggregateRoot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@AggregateRoot
@Getter
public class Restaurant {
    private final RestaurantId id;
    private final OwnerId ownerId;

    private final AddressId addressId;
    private final RestaurantType type;
    private final String name;
    private final String email;
    private final String logo;
    private final List<Dish> dishes =  new ArrayList<>();

    public Restaurant(OwnerId ownerId, AddressId addressId, RestaurantType type, String name, String email, String logo) {
        this.name = name;
        this.id = RestaurantId.create();
        this.ownerId = ownerId;
        this.addressId = addressId;
        this.type = type;
        this.email = email;
        this.logo = logo;
    }

    public List<Dish> getDishes() {
        return Collections.unmodifiableList(dishes);
    }
}
