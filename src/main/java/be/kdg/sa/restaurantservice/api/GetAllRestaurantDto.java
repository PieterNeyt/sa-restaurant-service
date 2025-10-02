package be.kdg.sa.restaurantservice.api;

import be.kdg.sa.restaurantservice.domain.restaurant.PriceCategory;
import be.kdg.sa.restaurantservice.domain.restaurant.Restaurant;
import be.kdg.sa.restaurantservice.domain.restaurant.RestaurantType;

import java.util.UUID;

public record GetAllRestaurantDto(UUID id,
                                  String name,
                                  RestaurantType restaurantType,
                                  boolean isOpen,
                                  PriceCategory priceCategory,
                                  String logo) {
    public static GetAllRestaurantDto from(Restaurant restaurant){
        return new GetAllRestaurantDto(
                restaurant.getId().id(),
                restaurant.getName(),
                restaurant.getType(),
                restaurant.isOpen(),
                restaurant.getPriceCategory(),
                restaurant.getLogo());
    }
}
