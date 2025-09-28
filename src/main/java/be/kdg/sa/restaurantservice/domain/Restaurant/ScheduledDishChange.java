package be.kdg.sa.restaurantservice.domain.Restaurant;

import lombok.Getter;
import org.jmolecules.ddd.annotation.Entity;

import java.time.LocalDateTime;

@Entity
@Getter
public class ScheduledDishChange {
    private final ScheduledDishChangeId id;
    private final DishId dishId;
    private final LocalDateTime scheduledTime;
    private final DishState targetState;

    public ScheduledDishChange(DishId dishId, LocalDateTime scheduledTime, DishState targetState) {
        this.id = ScheduledDishChangeId.create();
        this.dishId = dishId;
        this.scheduledTime = scheduledTime;
        this.targetState = targetState;
    }


    public ScheduledDishChange(ScheduledDishChangeId id, DishId dishId, LocalDateTime scheduledTime, DishState targetState) {
        this.id = id;
        this.dishId = dishId;
        this.scheduledTime = scheduledTime;
        this.targetState = targetState;
    }
}
