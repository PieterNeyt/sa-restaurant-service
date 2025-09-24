package be.kdg.sa.restaurantservice.api;

import be.kdg.sa.restaurantservice.domain.Restaurant.Dish;
import be.kdg.sa.restaurantservice.domain.Restaurant.DishState;
import be.kdg.sa.restaurantservice.domain.Restaurant.Restaurant;
import be.kdg.sa.restaurantservice.domain.Restaurant.RestaurantType;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record RestaurantDto(UUID id, UUID ownerId, UUID addressId, RestaurantType restaurantType,String name, String email, String logo, List<Dish> dishes) {
    public static RestaurantDto from(final Restaurant restaurant) {
        return new RestaurantDto(restaurant.getId().id(),restaurant.getOwnerId().id(),
                restaurant.getAddressId().id(), restaurant.getType(), restaurant.getName(), restaurant.getEmail(), restaurant.getLogo(), restaurant.getDishes());
    }
    public record DishDto(UUID id, UUID RestaurantId, String name, String description, BigDecimal price,
                          DishState dishState) {
        public static DishDto from(final Dish dish,UUID restaurantId) {
            return new DishDto(dish.getId().id(),restaurantId, dish.getName(), dish.getDescription(), dish.getPrice(), dish.getState());
        }
    }
}
