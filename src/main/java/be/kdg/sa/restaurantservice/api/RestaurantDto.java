package be.kdg.sa.restaurantservice.api;

import be.kdg.sa.restaurantservice.domain.Restaurant.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
                            PriceCategory priceCategory) {
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
                restaurant.getPriceCategory());
    }
    public record DishDto(UUID id, UUID RestaurantId, String name, String description, BigDecimal price,
                          DishState dishState) {
        public static DishDto from(final Dish dish,UUID restaurantId) {
            return new DishDto(
                    dish.getId().id(),
                    restaurantId,
                    dish.getName(),
                    dish.getDescription(),
                    dish.getPrice(),
                    dish.getState());
        }
    }

    public record ScheduleDishChangeRequest(UUID dishId, LocalDateTime scheduledTime, DishState targetState) {}

}
