package be.kdg.sa.restaurantservice.domain.schedulechange;

import be.kdg.sa.restaurantservice.domain.restaurant.dish.DishId;
import be.kdg.sa.restaurantservice.domain.restaurant.dish.DishState;
import lombok.Getter;
import org.jmolecules.ddd.annotation.Entity;
import org.jmolecules.ddd.annotation.Identity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
public class ScheduledDishChange {
    @Identity
    private final ScheduledDishChangeId id;
    private final DishId dishId;
    private final LocalDateTime scheduledTime;

    private final DishState targetState;
    private final String targetName;
    private final String targetDescription;
    private final BigDecimal targetPrice;
    private final int preparationTime;

    public ScheduledDishChange(DishId dishId,
                               LocalDateTime scheduledTime,
                               DishState targetState,
                               String targetName,
                               String targetDescription,
                               BigDecimal targetPrice,
                               int preparationTime) {
        this.id = ScheduledDishChangeId.create();
        this.dishId = dishId;
        this.scheduledTime = scheduledTime;
        this.targetState = targetState;
        this.targetName = targetName;
        this.targetDescription = targetDescription;
        this.targetPrice = targetPrice;
        this.preparationTime= preparationTime;
    }

    public ScheduledDishChange(ScheduledDishChangeId id,
                               DishId dishId,
                               LocalDateTime scheduledTime,
                               DishState targetState,
                               String targetName,
                               String targetDescription,
                               BigDecimal targetPrice,
                               int  preparationTime) {
        this.id = id;
        this.dishId = dishId;
        this.scheduledTime = scheduledTime;
        this.targetState = targetState;
        this.targetName = targetName;
        this.targetDescription = targetDescription;
        this.targetPrice = targetPrice;
        this.preparationTime = preparationTime;
    }
}

