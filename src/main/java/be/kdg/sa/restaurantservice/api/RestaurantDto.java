package be.kdg.sa.restaurantservice.api;

import be.kdg.sa.restaurantservice.domain.restaurant.*;
import be.kdg.sa.restaurantservice.domain.restaurant.dish.Dish;
import be.kdg.sa.restaurantservice.domain.restaurant.dish.DishState;
import be.kdg.sa.restaurantservice.domain.schedulechange.ScheduledDishChange;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public record RestaurantDto(UUID id,
                            UUID ownerId,
                            UUID addressId,
                            RestaurantType restaurantType,
                            String name,
                            String email,
                            String logo,
                            List<DishDto> dishes,
                            boolean isOpen,
                            PriceCategory priceCategory,
                            List<OpeningHourDto> openingHours
) {
    public static RestaurantDto from(final Restaurant restaurant) {
        return new RestaurantDto(
                restaurant.getId().id(),
                restaurant.getOwnerId().id(),
                restaurant.getAddressId().id(),
                restaurant.getType(),
                restaurant.getName(),
                restaurant.getEmail(),
                restaurant.getLogo(),
                restaurant.getDishes().stream()
                        .map(dish -> DishDto.from(dish, restaurant.getId().id()))
                        .toList(),
                restaurant.isOpen(),
                restaurant.getPriceCategory(),
                restaurant.getOpeningHours().stream()
                .map(OpeningHourDto::from)
                .toList());
    }
    public record DishDto(UUID id, UUID RestaurantId, String name, String description, BigDecimal price,
                          DishState dishState, int preparationTime) {
        public static DishDto from(final Dish dish, UUID restaurantId) {
            return new DishDto(
                    dish.getId().id(),
                    restaurantId,
                    dish.getName(),
                    dish.getDescription(),
                    dish.getPrice(),
                    dish.getState(),
                    dish.getPreparationTime());
        }
    }

    public record ScheduleDishChangeDto(
            UUID dishId,
            UUID id,
            LocalDateTime scheduledTime,
            DishState targetState,
            String targetName,
            String targetDescription,
            BigDecimal targetPrice,
            int targetPreparationTime
    ) {
        public static ScheduleDishChangeDto from(final ScheduledDishChange change) {
            return new ScheduleDishChangeDto(
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
    public record RestaurantChangesOverviewDto(
            UUID restaurantId,
            List<RestaurantDto.DishDto> liveDishes,
            List<RestaurantDto.ScheduleDishChangeDto> pendingChanges,
            int pendingCount
    ) {
        public static RestaurantChangesOverviewDto from(Restaurant restaurant, List<ScheduledDishChange> changes) {
            return new RestaurantChangesOverviewDto(
                    restaurant.getId().id(),
                    restaurant.getDishes().stream()
                            .map(dish -> RestaurantDto.DishDto.from(dish, restaurant.getId().id()))
                            .toList(),
                    changes.stream()
                            .map(RestaurantDto.ScheduleDishChangeDto::from)
                            .toList(),
                    changes.size()
            );
        }
    }

    public record OpeningHourDto(
            DayOfWeek dayOfWeek,
            LocalTime openingTime,
            LocalTime closingTime
    ) {
        public static OpeningHourDto from(OpeningHour openingHour) {
            return new OpeningHourDto(
                    openingHour.getDayOfWeek(),
                    openingHour.getOpeningTime(),
                    openingHour.getClosingTime()
            );
        }
    }
}
