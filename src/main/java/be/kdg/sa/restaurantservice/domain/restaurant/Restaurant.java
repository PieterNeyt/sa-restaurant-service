package be.kdg.sa.restaurantservice.domain.restaurant;

import be.kdg.sa.restaurantservice.domain.ActionNotPossibleException;
import be.kdg.sa.restaurantservice.domain.NotFoundException;
import be.kdg.sa.restaurantservice.domain.address.Address;
import be.kdg.sa.restaurantservice.domain.order.OrderLine;
import be.kdg.sa.restaurantservice.domain.owner.OwnerId;
import be.kdg.sa.restaurantservice.domain.restaurant.dish.Dish;
import be.kdg.sa.restaurantservice.domain.restaurant.dish.DishId;
import be.kdg.sa.restaurantservice.domain.restaurant.dish.DishState;
import lombok.Getter;
import org.jmolecules.ddd.annotation.AggregateRoot;
import org.jmolecules.ddd.annotation.Identity;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@AggregateRoot
@Getter
public class Restaurant {
    @Identity
    private final RestaurantId id;
    private final OwnerId ownerId;
    private final Address address;

    private final RestaurantType type;

    private final String name;
    private final String email;
    private final String logo;

    private final List<Dish> dishes = new ArrayList<>();
    private final List<OpeningHour> openingHours = new ArrayList<>();

    private boolean isOpen;
    private PriceCategory priceCategory;

    @Value("${restaurant.max.published_dishes}")
    private int MAX_PUBLISHED_DISHES;
    @Value("${restaurant.cheap.category}")
    private int MAX_AVG_PRICE_CHEAP_CATEGORY;
    @Value("${restaurant.normal.category}")
    private int MAX_AVG_PRICE_NORMAL_CATEGORY;
    @Value("${restaurant.expensive.category}")
    private int MAX_AVG_PRICE_EXPENSIVE_CATEGORY;


    public Restaurant(OwnerId ownerId, Address address, RestaurantType type, String name, String email, String logo) {
        this.name = name;
        this.id = RestaurantId.create();
        this.ownerId = ownerId;
        this.address = address;
        this.type = type;
        this.email = email;
        this.logo = logo;
        this.isOpen = false;
        this.priceCategory = PriceCategory.CHEAP;
    }

    public Restaurant(RestaurantId id, OwnerId ownerId, Address address,
                      RestaurantType type,
                      String name, String email, String logo,
                      boolean isOpen,
                      PriceCategory priceCategory) {
        this.id = id;
        this.ownerId = ownerId;
        this.address = address;
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


    public void addDish(UUID id, String description, String name, DishState state, BigDecimal price, int preparationTime) {
        dishes.add(new Dish(new DishId(id), state, name, description, price, preparationTime));
        updatePriceCategory();
    }

    public void updateDishState(UUID dishId, DishState state) {
        //get Dish
        Dish dish = dishes.stream().filter(d -> d.getId().id().equals(dishId)).findFirst()
                .orElseThrow(() -> new NotFoundException("dish not found"));

        if (state == DishState.PUBLISHED && hasMaximumPublishedDishes())
            throw new ActionNotPossibleException("Maximum amount of dishes achieved (10)");

        dish.changeStateTo(state);
        updatePriceCategory();
    }

    private boolean hasMaximumPublishedDishes() {
        return dishes.stream()
                .filter(d -> d.getState() == DishState.PUBLISHED)
                .count() >= MAX_PUBLISHED_DISHES;
    }

    public void changeOpenState(UUID requesterId) {
        if (!ownerId.id().equals(requesterId)) {
            throw new ActionNotPossibleException("Only the restaurant owner can change the open state");
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

        if (avgPrice < MAX_AVG_PRICE_CHEAP_CATEGORY) {
            this.priceCategory = PriceCategory.CHEAP;
        } else if (avgPrice <= MAX_AVG_PRICE_NORMAL_CATEGORY) {
            this.priceCategory = PriceCategory.NORMAL;
        } else if (avgPrice <= MAX_AVG_PRICE_EXPENSIVE_CATEGORY) {
            this.priceCategory = PriceCategory.EXPENSIVE;
        } else {
            this.priceCategory = PriceCategory.PREMIUM;
        }
    }

    public void changeDish(DishId dishId, DishState targetState, String targetName, BigDecimal targetPrice, String targetDescription, int targetPreparationTime) {
        dishes.stream()
                .filter(d -> d.getId().id().equals(dishId.id()))
                .findFirst().ifPresent(dish -> {
                    updateDishState(dishId.id(),targetState);
                    dish.changePriceTo(targetPrice);
                    dish.changeDescriptionTo(targetDescription);
                    dish.changeNameTo(targetName);
                    dish.changePreparationTime(targetPreparationTime);
                });
        updatePriceCategory();
    }
    public OpeningHour addOpeningHour(DayOfWeek dayOfWeek, LocalTime openingTime, LocalTime closingTime) {
        OpeningHour openingHour = new OpeningHour(dayOfWeek, openingTime, closingTime);

        Optional<OpeningHour> existing = openingHours.stream()
                .filter(o -> o.getDayOfWeek() == openingHour.getDayOfWeek())
                .findFirst();

        if (existing.isPresent()) throw new ActionNotPossibleException("Opening hours for this day already exist");

        openingHours.add(openingHour);
        return openingHour;
    }

    public void isOwner(UUID ownerId) {
        if (!this.ownerId.id().equals(ownerId)) {
            throw new SecurityException("Not allowed to view changes for this restaurant");
        }
    }

    public RestaurantType getRestaurantType() {
        return this.type;
    }

    public void prepareCheckout(List<OrderLine> items) {
        DayOfWeek today = LocalDate.now().getDayOfWeek();
        var openingHoursToday = openingHours.stream()
                .filter(oh -> oh.getDayOfWeek() == today)
                .findFirst()
                .orElseThrow(() -> new NotFoundException("No opening hours available for today"));

        var now = LocalTime.now();

        int maxPreparationMinutes = items.stream()
                .mapToInt(OrderLine::preparationTime)
                .max()
                .orElse(0);

        var expectedFinishTime = now.plusMinutes(maxPreparationMinutes);

        if (!openingHoursToday.isOpenAt(now) || expectedFinishTime.isAfter(openingHoursToday.getClosingTime())) {
          throw new NotFoundException("Restaurant is closed or order cannot be fulfilled onetime");
        }
    }

    public void checkDish(OrderLine item) {
        var dish = dishes.stream()
                .filter(d -> d.getId().id().equals(item.dishId()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Dish not found"));


        if (dish.getPrice().compareTo(item.price()) != 0 ||
                !dish.getName().equals(item.name()) ||
                dish.getPreparationTime() != item.preparationTime()) {

            throw new ActionNotPossibleException("The attributes of one or more dishes has changed ");
        }

        if (dish.getState() != DishState.PUBLISHED) {
            throw new ActionNotPossibleException("Dish " + dish.getName() + " is not available right now.");
        }
    }
}
