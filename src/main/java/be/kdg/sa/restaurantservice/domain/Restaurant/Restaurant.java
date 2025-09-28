package be.kdg.sa.restaurantservice.domain.Restaurant;

import be.kdg.sa.restaurantservice.domain.Address.AddressId;
import be.kdg.sa.restaurantservice.domain.Owner.OwnerId;
import lombok.Getter;
import org.jmolecules.ddd.annotation.AggregateRoot;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

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
    private final List<Dish> dishes = new ArrayList<>();

    private boolean isOpen;
    private PriceCategory priceCategory;

    public Restaurant(OwnerId ownerId, AddressId addressId, RestaurantType type, String name, String email, String logo) {
        this.name = name;
        this.id = RestaurantId.create();
        this.ownerId = ownerId;
        this.addressId = addressId;
        this.type = type;
        this.email = email;
        this.logo = logo;
        this.isOpen = false;
        this.priceCategory = PriceCategory.CHEAP; // default cheap
    }

    public Restaurant(RestaurantId id, OwnerId ownerId, AddressId addressId, RestaurantType type, String name, String email, String logo, boolean isOpen, PriceCategory priceCategory) {
        this.name = name;
        this.id = id;
        this.ownerId = ownerId;
        this.addressId = addressId;
        this.type = type;
        this.email = email;
        this.logo = logo;
        this.isOpen = isOpen;
        this.priceCategory = priceCategory;
    }

    public List<Dish> getDishes() {
        return Collections.unmodifiableList(dishes);
    }


    public void addDish(UUID id, String description, String name, DishState state, BigDecimal price) {
        dishes.add(new Dish(new DishId(id), state, name, description, price));
        updatePriceCategory();
    }

    public void updateDish(UUID dishId, DishState state) {
        //get Dish
        Dish dish = dishes.stream().filter(d -> d.getId().id().equals(dishId)).findFirst().orElseThrow();

        if (state == DishState.PUBLISHED && hasMaximumPublishedDishes())
            throw new RuntimeException("Maximum aantal published dishes bereikt (10)");

        dish.setState(state);
        updatePriceCategory();
    }

    private boolean hasMaximumPublishedDishes() {
        return dishes.stream()
                .filter(d -> d.getState() == DishState.PUBLISHED)
                .count() >= 10;
    }

    public void changeOpenState(UUID requesterId) {
        if (!ownerId.id().equals(requesterId)) {
            throw new IllegalStateException("Enkel de eigenaar kan de openingsstatus wijzigen");
        }
        this.isOpen = !isOpen;
    }
    public void updatePriceCategory() {

        if (dishes.isEmpty()) {
            this.priceCategory = PriceCategory.CHEAP; // als geen gerechten default cheap
            return;
        }

        double sum = 0;
        for (Dish dish : dishes) {
                sum += dish.getPrice().doubleValue();
        }
        double avgPrice = sum / dishes.size();

        if (avgPrice < 10) {
            this.priceCategory = PriceCategory.CHEAP;
        } else if (avgPrice <= 30) {
            this.priceCategory = PriceCategory.NORMAL;
        } else if (avgPrice <= 60) {
            this.priceCategory = PriceCategory.EXPENSIVE;
        } else {
            this.priceCategory = PriceCategory.PREMIUM;
        }
    }

}
