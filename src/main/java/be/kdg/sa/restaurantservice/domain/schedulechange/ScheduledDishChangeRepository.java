package be.kdg.sa.restaurantservice.domain.schedulechange;


import be.kdg.sa.restaurantservice.domain.restaurant.dish.Dish;
import be.kdg.sa.restaurantservice.domain.restaurant.dish.DishId;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface ScheduledDishChangeRepository {
    void save(ScheduledDishChange change);

    List<ScheduledDishChange> findDueChanges(LocalDateTime now);
    void delete(ScheduledDishChange change);


    List<ScheduledDishChange> findDueChangesByRestaurantAndOwner(UUID restaurantId, UUID ownerId);

}