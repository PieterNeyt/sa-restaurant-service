package be.kdg.sa.restaurantservice.domain.Restaurant;



import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


public interface ScheduledDishChangeRepository {
    void save(ScheduledDishChange change);

    List<ScheduledDishChange> findDueChanges(LocalDateTime now);
    void delete(ScheduledDishChange change);

    List<ScheduledDishChange> findPendingChanges(LocalDateTime now);

    List<ScheduledDishChange> findDueChangesByRestaurantAndOwner(UUID restaurantId, UUID ownerId);
}