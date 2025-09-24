package be.kdg.sa.restaurantservice.api;

import be.kdg.sa.restaurantservice.domain.Restaurant.Dish;
import be.kdg.sa.restaurantservice.domain.Restaurant.Restaurant;
import be.kdg.sa.restaurantservice.domain.Restaurant.RestaurantType;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

public record RestaurantDto(UUID id, UUID ownerId, UUID addressId, RestaurantType restaurantType,String name, String email, String logo, List<Dish> dishes) {
    public static RestaurantDto from(final Restaurant restaurant) {
        return new RestaurantDto(restaurant.getId().id(),restaurant.getOwnerId().id(),
                restaurant.getAddressId().id(), restaurant.getType(), restaurant.getName(), restaurant.getEmail(), restaurant.getLogo(), restaurant.getDishes());
    }
}
