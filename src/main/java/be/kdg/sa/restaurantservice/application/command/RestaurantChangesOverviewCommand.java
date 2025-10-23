package be.kdg.sa.restaurantservice.application.command;

import be.kdg.sa.restaurantservice.api.dto.RestaurantDto;
import be.kdg.sa.restaurantservice.domain.restaurant.Restaurant;
import be.kdg.sa.restaurantservice.domain.restaurant.dish.Dish;
import be.kdg.sa.restaurantservice.domain.restaurant.dish.DishState;
import be.kdg.sa.restaurantservice.domain.schedulechange.ScheduledDishChange;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record RestaurantChangesOverviewCommand(UUID restaurantId,
                                               List<DishCommand> liveDishes,
                                               List<ScheduleDishChangeCommand> pendingChanges,
                                               int pendingCount) {

    public static RestaurantChangesOverviewCommand from(Restaurant restaurant, List<ScheduledDishChange> changes) {
        return new RestaurantChangesOverviewCommand(
                restaurant.getId().id(),
                restaurant.getDishes().stream()
                        .map(dish -> DishCommand.from(dish, restaurant.getId().id()))
                        .toList(),
                changes.stream()
                        .map(ScheduleDishChangeCommand::from)
                        .toList(),
                changes.size()
        );
    }
    public record DishCommand(UUID id, UUID RestaurantId, String name, String description, BigDecimal price,
                              DishState dishState, int preparationTime) {

        public static DishCommand from(final Dish dish, UUID restaurantId) {
            return new DishCommand(
                    dish.getId().id(),
                    restaurantId,
                    dish.getName(),
                    dish.getDescription(),
                    dish.getPrice(),
                    dish.getState(),
                    dish.getPreparationTime());
        }
    }
    public record ScheduleDishChangeCommand(
            UUID dishId,
            UUID id,
            LocalDateTime scheduledTime,
            DishState targetState,
            String targetName,
            String targetDescription,
            BigDecimal targetPrice,
            int targetPreparationTime
    ) {
        public static ScheduleDishChangeCommand from(final ScheduledDishChange change) {
            return new ScheduleDishChangeCommand(
                    change.getId().id(),
                    change.getDishId().id(),
                    change.getScheduledTime(),
                    change.getTargetState(),
                    change.getTargetName(),
                    change.getTargetDescription(),
                    change.getTargetPrice(),
                    change.getPreparationTime()
            );
        }
    }
}