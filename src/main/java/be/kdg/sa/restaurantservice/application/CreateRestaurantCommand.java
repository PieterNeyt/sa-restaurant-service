package be.kdg.sa.restaurantservice.application;

import be.kdg.sa.restaurantservice.domain.Restaurant.Dish;
import be.kdg.sa.restaurantservice.domain.Restaurant.DishState;
import be.kdg.sa.restaurantservice.domain.Restaurant.RestaurantType;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record CreateRestaurantCommand(UUID ownerId,
                                      UUID addressId,
                                      RestaurantType restaurantType,
                                      String name,
                                      String email,
                                      String logo,
                                      List<Dish> dishes) {
    public record CreateDishCommand(UUID restaurantId,
                                          String name,
                                          String description,
                                          BigDecimal price,
                                          DishState dishState
                                          ) {

    }

}
