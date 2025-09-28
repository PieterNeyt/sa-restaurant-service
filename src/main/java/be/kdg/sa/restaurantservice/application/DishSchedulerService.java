package be.kdg.sa.restaurantservice.application;

import be.kdg.sa.restaurantservice.domain.Restaurant.DishRepository;
import be.kdg.sa.restaurantservice.domain.Restaurant.ScheduledDishChange;
import be.kdg.sa.restaurantservice.domain.Restaurant.ScheduledDishChangeRepository;
import be.kdg.sa.restaurantservice.infrastructure.restaurant.jpa.JpaDishRepository;
import be.kdg.sa.restaurantservice.infrastructure.restaurant.jpa.JpaScheduledDishChangeEntity;
import be.kdg.sa.restaurantservice.infrastructure.restaurant.jpa.JpaScheduledDishChangeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class DishSchedulerService {


    private final ScheduledDishChangeRepository scheduledRepo;

    private final DishRepository dishRepo;

    @Scheduled(fixedRate = 60000) // elke minuut
    @Transactional
    public void executeScheduledChanges() {
        List<ScheduledDishChange> dueChanges = scheduledRepo.findDueChanges(LocalDateTime.now());

        for (ScheduledDishChange change : dueChanges) {
            dishRepo.findById(change.getDishId()).ifPresentOrElse(dish -> {
                dish.setState(change.getTargetState());
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