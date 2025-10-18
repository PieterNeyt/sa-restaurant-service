package be.kdg.sa.restaurantservice.application.command;

import be.kdg.sa.restaurantservice.api.dto.RestaurantDto;
import be.kdg.sa.restaurantservice.domain.restaurant.RestaurantType;

import java.util.List;
import java.util.UUID;

public record CreateRestaurantCommand(UUID ownerId,
                                      RestaurantType restaurantType,
                                      String name,
                                      String email,
                                      String logo,
                                      List<RestaurantDto.DishDto> dishes,
                                      String city,
                                      String streetNumber,
                                      String street,
                                      String postalCode,
                                      String country
                                      ) {

}

