package be.kdg.sa.restaurantservice.application;

import be.kdg.sa.restaurantservice.api.RestaurantDto;
import be.kdg.sa.restaurantservice.domain.Restaurant.DishId;
import be.kdg.sa.restaurantservice.domain.Restaurant.DishRepository;
import be.kdg.sa.restaurantservice.domain.Restaurant.ScheduledDishChange;
import be.kdg.sa.restaurantservice.domain.Restaurant.ScheduledDishChangeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ScheduledDishChangeService {
    private final ScheduledDishChangeRepository scheduledRepo;
    private final DishRepository dishRepo;

    public ScheduledDishChangeService(ScheduledDishChangeRepository scheduledRepo,  DishRepository dishRepo) {
        this.scheduledRepo = scheduledRepo;
        this.dishRepo = dishRepo;
    }

    public void scheduleDishChange(RestaurantDto.ScheduleDishChangeRequest request) {
        ScheduledDishChange change = new ScheduledDishChange(
                new DishId(request.dishId()),
                request.scheduledTime(),
                request.targetState(),
                request.targetName(),
                request.targetDescription(),
                request.targetPrice()
        );
        scheduledRepo.save(change);
    }

    @Transactional
    public void applyAllPendingChanges(UUID ownerId, UUID restaurantId) {

        List<ScheduledDishChange> changes = scheduledRepo.findDueChangesByRestaurantAndOwner(
                 restaurantId, ownerId
        );

        for (ScheduledDishChange change : changes) {
            dishRepo.findById(change.getDishId()).ifPresent(dish -> {
                dish.setState(change.getTargetState());
                dish.setName(change.getTargetName());
                dish.setDescription(change.getTargetDescription());
                dish.setPrice(change.getTargetPrice());

                dishRepo.save(dish);
                scheduledRepo.delete(change);
            });
        }
    }



}