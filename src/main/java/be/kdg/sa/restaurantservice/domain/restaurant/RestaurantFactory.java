package be.kdg.sa.restaurantservice.domain.restaurant;

import be.kdg.sa.restaurantservice.domain.address.Address;
import be.kdg.sa.restaurantservice.domain.owner.OwnerId;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RestaurantFactory {
    private final int maxPublishedDishes;
    private final int maxAvgPriceCheapCategory;
    private final int maxAvgPriceNormalCategory;
    private final int maxAvgPriceExpensiveCategory;

    public RestaurantFactory(
            @Value("${restaurant.max.published_dishes}") int maxPublishedDishes,
            @Value("${restaurant.cheap.category}") int maxAvgPriceCheapCategory,
            @Value("${restaurant.normal.category}") int maxAvgPriceNormalCategory,
            @Value("${restaurant.expensive.category}") int maxAvgPriceExpensiveCategory) {
        this.maxPublishedDishes = maxPublishedDishes;
        this.maxAvgPriceCheapCategory = maxAvgPriceCheapCategory;
        this.maxAvgPriceNormalCategory = maxAvgPriceNormalCategory;
        this.maxAvgPriceExpensiveCategory = maxAvgPriceExpensiveCategory;
    }

    // Voor nieuwe restaurants
    public Restaurant create(OwnerId ownerId, Address address, RestaurantType type,
                             String name, String email, String logo) {
        return new Restaurant(ownerId, address, type, name, email, logo,
                maxPublishedDishes, maxAvgPriceCheapCategory,
                maxAvgPriceNormalCategory, maxAvgPriceExpensiveCategory);
    }

    // Voor bestaande restaurants uit de database
    public Restaurant reconstitute(RestaurantId id, OwnerId ownerId, Address address,
                                   RestaurantType type, String name, String email, String logo,
                                   boolean isOpen, PriceCategory priceCategory) {
        return new Restaurant(id, ownerId, address, type, name, email, logo,
                isOpen, priceCategory,
                maxPublishedDishes, maxAvgPriceCheapCategory,
                maxAvgPriceNormalCategory, maxAvgPriceExpensiveCategory);
    }
}