package be.kdg.sa.restaurantservice.application;

import be.kdg.sa.restaurantservice.domain.restaurant.Restaurant;
import be.kdg.sa.restaurantservice.domain.restaurant.RestaurantRepository;
import be.kdg.sa.restaurantservice.domain.schedulechange.ScheduledDishChange;
import be.kdg.sa.restaurantservice.domain.schedulechange.ScheduledDishChangeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class RestaurantSchedulerService {


    private final ScheduledDishChangeRepository scheduledRepo;
    private final RestaurantRepository restRepo;

    @Scheduled(fixedRate = 60000) // elke minuut
    public void executeScheduledDishChanges() {
        List<ScheduledDishChange> dueChanges = scheduledRepo.findDueChanges(LocalDateTime.now());

        for (ScheduledDishChange change : dueChanges) {
            Restaurant restaurant = restRepo.findRestaurantFromDishId(change.getDishId().id())
                    .orElseThrow(() -> new RuntimeException("Restaurant not found"));
            restaurant.updateDish(
                    change.getDishId(),
                    change.getTargetState(),
                    change.getTargetName(),
                    change.getTargetPrice(),
                    change.getTargetDescription(),
                    change.getPreparationTime());

            restRepo.save(restaurant);
            scheduledRepo.save(change);
        }
    }

}