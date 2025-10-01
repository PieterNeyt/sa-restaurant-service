package be.kdg.sa.restaurantservice.domain.restaurant;

import lombok.Getter;
import org.jmolecules.ddd.annotation.Entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
public class ScheduledDishChange {
    private final ScheduledDishChangeId id;
    private final DishId dishId;
    private final LocalDateTime scheduledTime;

    private final DishState targetState;
    private final String targetName;
    private final String targetDescription;
    private final BigDecimal targetPrice;

    public ScheduledDishChange(DishId dishId,
                               LocalDateTime scheduledTime,
                               DishState targetState,
                               String targetName,
                               String targetDescription,
                               BigDecimal targetPrice) {
        this.id = ScheduledDishChangeId.create();
        this.dishId = dishId;
        this.scheduledTime = scheduledTime;
        this.targetState = targetState;
        this.targetName = targetName;
        this.targetDescription = targetDescription;
        this.targetPrice = targetPrice;
    }

    public ScheduledDishChange(ScheduledDishChangeId id,
                               DishId dishId,
                               LocalDateTime scheduledTime,
                               DishState targetState,
                               String targetName,
                               String targetDescription,
                               BigDecimal targetPrice) {
        this.id = id;
        this.dishId = dishId;
        this.scheduledTime = scheduledTime;
        this.targetState = targetState;
        this.targetName = targetName;
        this.targetDescription = targetDescription;
        this.targetPrice = targetPrice;
    }
}

