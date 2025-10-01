package be.kdg.sa.restaurantservice.domain.restaurant;

import be.kdg.sa.restaurantservice.domain.address.AddressId;
import be.kdg.sa.restaurantservice.domain.owner.OwnerId;
import lombok.Getter;
import org.jmolecules.ddd.annotation.AggregateRoot;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.time.*;
import java.util.*;

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
    private final List<OpeningHour> openingHours = new ArrayList<>();

    private boolean isOpen;
    private PriceCategory priceCategory;

    public Restaurant(OwnerId ownerId, AddressId addressId, RestaurantType type, String name, String email, String logo) {
        Assert.notNull(ownerId, "owner id must not be null");
        Assert.notNull(addressId, "address id must not be null");

        Assert.hasText(name, "name must not be blank");
        Assert.hasText(email, "email must not be blank");
        Assert.hasText(logo, "logo must not be blank");

        Assert.notNull(type, "restaurant type must not be null");

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

    public Restaurant(RestaurantId id, OwnerId ownerId, AddressId addressId,
                      RestaurantType type,
                      String name, String email, String logo,
                      boolean isOpen,
                      PriceCategory priceCategory) {
        Assert.notNull(id, "id must not be null");
        Assert.notNull(ownerId, "owner id must not be null");
        Assert.notNull(addressId, "address id must not be null");

        Assert.hasText(name, "name must not be blank");
        Assert.hasText(email, "email must not be blank");
        Assert.hasText(logo, "logo must not be blank");

        Assert.notNull(type, "restaurant type must not be null");
        Assert.notNull(priceCategory, "priceCategory must not be null");

        this.id = id;
        this.ownerId = ownerId;
        this.addressId = addressId;
        this.name = name;
        this.email = email;
        this.logo = logo;
        this.type = type;
        this.priceCategory = priceCategory;
        this.isOpen = isOpen;
    }

    public List<Dish> getDishes() {
        return Collections.unmodifiableList(dishes);
    }


    public void addDish(UUID id, String description, String name, DishState state, BigDecimal price) {
        dishes.add(new Dish(new DishId(id), state, name, description, price));
        updatePriceCategory();
    }

    public void updateDishState(UUID dishId, DishState state) {
        //get Dish
        Dish dish = dishes.stream().filter(d -> d.getId().id().equals(dishId)).findFirst().orElseThrow();

        if (state == DishState.PUBLISHED && hasMaximumPublishedDishes())
            throw new RuntimeException("Maximum aantal published dishes bereikt (10)");

        dish.changeStateTo(state);
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

    public void updateDish(DishId dishId, DishState targetState, String targetName, BigDecimal targetPrice, String targetDescription) {
        dishes.stream()
                .filter(d -> d.getId().id().equals(dishId.id()))
                .findFirst().ifPresent(dish -> {
                    updateDishState(dishId.id(),targetState);
                    dish.changePriceTo(targetPrice);
                    dish.changeDescriptionTo(targetDescription);
                    dish.changeNameTo(targetName);
                });
        updatePriceCategory();
    }

    public void addOpeningHour(OpeningHour openingHour) {
        Optional<OpeningHour> existing = openingHours.stream()
                .filter(o -> o.getDayOfWeek() == openingHour.getDayOfWeek())
                .findFirst();

        if (existing.isPresent()) throw new IllegalArgumentException("Opening hours for this day already exist");

        openingHours.add(openingHour);
    }

    public void changeOpeningStatus(){
        ZonedDateTime now = ZonedDateTime.now(ZoneId.systemDefault());
        DayOfWeek today = now.getDayOfWeek();
        LocalTime currentTime = now.toLocalTime();

        this.isOpen = openingHours.stream()
                .filter(o -> o.getDayOfWeek() == today)
                .anyMatch(o -> o.isOpenAt(currentTime));
    }
}
