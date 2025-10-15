package be.kdg.sa.restaurantservice.application.command;

import be.kdg.sa.restaurantservice.api.dto.RestaurantDto;
import be.kdg.sa.restaurantservice.domain.restaurant.RestaurantType;

import java.util.List;
import java.util.UUID;

public record CreateRestaurantCommand(UUID ownerId,
                                      UUID addressId,
                                      RestaurantType restaurantType,
                                      String name,
                                      String email,
                                      String logo,
                                      List<RestaurantDto.DishDto> dishes) {

}

