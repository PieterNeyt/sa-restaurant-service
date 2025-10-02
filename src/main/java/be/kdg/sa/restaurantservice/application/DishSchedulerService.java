package be.kdg.sa.restaurantservice.application;

import be.kdg.sa.restaurantservice.domain.Restaurant.DishRepository;
import be.kdg.sa.restaurantservice.domain.Restaurant.RestaurantRepository;
import be.kdg.sa.restaurantservice.domain.Restaurant.ScheduledDishChange;
import be.kdg.sa.restaurantservice.domain.Restaurant.ScheduledDishChangeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DishSchedulerService {


    private final ScheduledDishChangeRepository scheduledRepo;

    private final DishRepository dishRepo;
    private final RestaurantRepository restRepo;

    @Scheduled(fixedRate = 60000) // elke minuut
    public void executeScheduledDishChanges() {
        List<ScheduledDishChange> dueChanges = scheduledRepo.findDueChanges(LocalDateTime.now());

        for (ScheduledDishChange change : dueChanges) {
            dishRepo.findById(change.getDishId()).ifPresentOrElse(dish -> {
                dish.changeStateTo(change.getTargetState());
                dish.changeNameTo(change.getTargetName());
                dish.changeDescriptionTo(change.getTargetDescription());
                dish.changePriceTo(change.getTargetPrice());

                dishRepo.save(dish);
                scheduledRepo.delete(change);

            }, () -> {

            });
        }
    }

}