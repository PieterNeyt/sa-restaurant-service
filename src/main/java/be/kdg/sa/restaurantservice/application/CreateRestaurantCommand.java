package be.kdg.sa.restaurantservice.application;

import be.kdg.sa.restaurantservice.domain.Restaurant.RestaurantType;

import java.util.List;
import java.util.UUID;

public record CreateRestaurantCommand(UUID ownerId,
                                      UUID addressId,
                                      RestaurantType restaurantType,
                                      String name,
                                      String email,
                                      String logo,
                                      List<be.kdg.sa.restaurantservice.api.RestaurantDto.DishDto> dishes) {

}

