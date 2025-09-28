package be.kdg.sa.restaurantservice.domain.Restaurant;



import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;


public interface ScheduledDishChangeRepository {
    void save(ScheduledDishChange change);

    List<ScheduledDishChange> findDueChanges(LocalDateTime now);
    void delete(ScheduledDishChange change);
}