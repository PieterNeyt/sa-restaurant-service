package be.kdg.sa.restaurantservice.application;

import be.kdg.sa.restaurantservice.api.RestaurantDto;
import be.kdg.sa.restaurantservice.domain.Restaurant.DishId;
import be.kdg.sa.restaurantservice.domain.Restaurant.ScheduledDishChange;
import be.kdg.sa.restaurantservice.domain.Restaurant.ScheduledDishChangeRepository;
import org.springframework.stereotype.Service;

@Service
public class ScheduledDishChangeService {
    private final ScheduledDishChangeRepository scheduledRepo;

    public ScheduledDishChangeService(ScheduledDishChangeRepository scheduledRepo) {
        this.scheduledRepo = scheduledRepo;
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

}