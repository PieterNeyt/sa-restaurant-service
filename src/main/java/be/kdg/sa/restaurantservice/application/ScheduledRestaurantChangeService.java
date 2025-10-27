package be.kdg.sa.restaurantservice.application;

import be.kdg.sa.restaurantservice.api.dto.RestaurantDto;
import be.kdg.sa.restaurantservice.domain.restaurant.*;
import be.kdg.sa.restaurantservice.domain.restaurant.dish.DishId;
import be.kdg.sa.restaurantservice.domain.schedulechange.ScheduledDishChange;
import be.kdg.sa.restaurantservice.domain.schedulechange.ScheduledDishChangeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class ScheduledRestaurantChangeService {

    private final ScheduledDishChangeRepository scheduledRepo;
    private final RestaurantRepository restRepo;

    public ScheduledRestaurantChangeService(ScheduledDishChangeRepository scheduledRepo, RestaurantRepository restRepo) {
        this.scheduledRepo = scheduledRepo;
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
            restaurant.changeDish(change.getDishId(),
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