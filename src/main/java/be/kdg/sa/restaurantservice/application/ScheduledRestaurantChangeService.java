package be.kdg.sa.restaurantservice.application;

import be.kdg.sa.restaurantservice.api.RestaurantDto;
import be.kdg.sa.restaurantservice.domain.restaurant.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class ScheduledRestaurantChangeService {
    private final ScheduledDishChangeRepository scheduledRepo;
    private final DishRepository dishRepo;
    private final RestaurantRepository restRepo;

    public ScheduledRestaurantChangeService(ScheduledDishChangeRepository scheduledRepo, DishRepository dishRepo, RestaurantRepository restRepo) {
        this.scheduledRepo = scheduledRepo;
        this.dishRepo = dishRepo;
        this.restRepo = restRepo;
    }

    public void scheduleDishChange(RestaurantDto.ScheduleDishChangeDto request) {
        ScheduledDishChange change = new ScheduledDishChange(
                new DishId(request.dishId()),
                request.scheduledTime(),
                request.targetState(),
                request.targetName(),
                request.targetDescription(),
                request.targetPrice(),
                request.targetPreparationTime()

        );
        scheduledRepo.save(change);
    }


    public void applyAllPendingChanges(UUID ownerId, UUID restaurantId) {

        List<ScheduledDishChange> changes = scheduledRepo.findDueChangesByRestaurantAndOwner(
                 restaurantId, ownerId
        );
        Restaurant restaurant = restRepo.findById(restaurantId).orElseThrow();
        for (ScheduledDishChange change : changes) {
            restaurant.updateDish(change.getDishId(),
                    change.getTargetState(),
                    change.getTargetName(),
                    change.getTargetPrice(),
                    change.getTargetDescription(),
                    change.getPreparationTime());

                scheduledRepo.delete(change);
        }
        restRepo.save(restaurant);
    }





}