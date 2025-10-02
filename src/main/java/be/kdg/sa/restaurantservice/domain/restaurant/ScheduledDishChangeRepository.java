package be.kdg.sa.restaurantservice.domain.restaurant;


import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


public interface ScheduledDishChangeRepository {
    void save(ScheduledDishChange change);

    List<ScheduledDishChange> findDueChanges(LocalDateTime now);
    void delete(ScheduledDishChange change);


    List<ScheduledDishChange> findDueChangesByRestaurantAndOwner(UUID restaurantId, UUID ownerId);
}