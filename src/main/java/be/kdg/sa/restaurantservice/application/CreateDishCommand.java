package be.kdg.sa.restaurantservice.application;

import be.kdg.sa.restaurantservice.domain.Restaurant.DishState;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateDishCommand(UUID restaurantId,
                                String name,
                                String description,
                                BigDecimal price,
                                DishState dishState
) {

}
