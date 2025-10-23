package be.kdg.sa.restaurantservice.api.dto;

import be.kdg.sa.restaurantservice.application.command.RestaurantChangesOverviewCommand;
import be.kdg.sa.restaurantservice.domain.address.Address;
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

public record RestaurantDto(
        UUID id,
        UUID ownerId,
        AddressDto address,
        RestaurantType restaurantType,
        String name,
        String email,
        String logo,
        List<DishDto> dishes,
        boolean isOpen,
        PriceCategory priceCategory,
        List<OpeningHourDto> openingHours
) {
    public record AddressDto(
            String street,
            String streetNumber,
            String city,
            String postalCode,
            String country
    ) {
        public static AddressDto from(Address address) {
            if (address == null) return null;
            return new AddressDto(
                    address.getStreet(),
                    address.getStreetNumber(),
                    address.getCity(),
                    address.getPostalCode(),
                    address.getCountry()
            );
        }
    }
    public static RestaurantDto from(final Restaurant restaurant) {
        Address address = restaurant.getAddress();

        return new RestaurantDto(
                restaurant.getId().id(),
                restaurant.getOwnerId().id(),
                AddressDto.from(restaurant.getAddress()),
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
                        .toList()
        );
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

        public static DishDto fromChanges(RestaurantChangesOverviewCommand.DishCommand dishCommand) {
            return new DishDto(
                    dishCommand.id(),
                    dishCommand.RestaurantId(),
                    dishCommand.name(),
                    dishCommand.description(),
                    dishCommand.price(),
                    dishCommand.dishState(),
                    dishCommand.preparationTime());
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

        public static ScheduleDishChangeDto fromChanges(RestaurantChangesOverviewCommand.ScheduleDishChangeCommand scheduleDishChangeCommand) {
            return new ScheduleDishChangeDto(
                    scheduleDishChangeCommand.id(),
                    scheduleDishChangeCommand.dishId(),
                    scheduleDishChangeCommand.scheduledTime(),
                    scheduleDishChangeCommand.targetState(),
                    scheduleDishChangeCommand.targetName(),
                    scheduleDishChangeCommand.targetDescription(),
                    scheduleDishChangeCommand.targetPrice(),
                    scheduleDishChangeCommand.targetPreparationTime()
            );
        }
    }
    public record RestaurantChangesOverviewDto(
            UUID restaurantId,
            List<RestaurantDto.DishDto> liveDishes,
            List<RestaurantDto.ScheduleDishChangeDto> pendingChanges,
            int pendingCount
    ) {
        public static RestaurantChangesOverviewDto from(RestaurantChangesOverviewCommand restaurantChangesOverviewCommand) {
            return new RestaurantChangesOverviewDto(
                    restaurantChangesOverviewCommand.restaurantId(),
                    restaurantChangesOverviewCommand.liveDishes().stream().map(DishDto::fromChanges).toList(),
                    restaurantChangesOverviewCommand.pendingChanges().stream().map(ScheduleDishChangeDto::fromChanges).toList(),
                    restaurantChangesOverviewCommand.pendingCount()
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
