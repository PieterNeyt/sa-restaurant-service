package be.kdg.sa.restaurantservice.application.command;

import be.kdg.sa.restaurantservice.domain.restaurant.dish.DishState;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateDishCommand(UUID restaurantId,
                                String name,
                                String description,
                                BigDecimal price,
                                DishState dishState,
                                int preparationTime
) {

}
