package be.kdg.sa.restaurantservice.infrastructure.restaurant.jpa;

import be.kdg.sa.restaurantservice.domain.Restaurant.DishId;
import be.kdg.sa.restaurantservice.domain.Restaurant.DishState;
import be.kdg.sa.restaurantservice.domain.Restaurant.ScheduledDishChange;
import be.kdg.sa.restaurantservice.domain.Restaurant.ScheduledDishChangeId;
import jakarta.persistence.*;
import lombok.Getter;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Table(name = "scheduled_dish_change")
public class JpaScheduledDishChangeEntity {
    @Id
    private UUID id;

    @Column(nullable = false)
    private UUID dishId;

    @Column(nullable = false)
    private LocalDateTime scheduledTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DishState targetState;
    @Column(nullable = false)
    private String targetName;
    @Column(nullable = false)
    private String targetDescription;
    @Column(nullable = false)
    private BigDecimal targetPrice;

    protected JpaScheduledDishChangeEntity() {}

    public JpaScheduledDishChangeEntity(UUID id, UUID dishId,
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

    public static JpaScheduledDishChangeEntity fromDomain(ScheduledDishChange change) {
        return new JpaScheduledDishChangeEntity(
                change.getId().id(),
                change.getDishId().id(),
                change.getScheduledTime(),
                change.getTargetState(),
                change.getTargetName(),
                change.getTargetDescription(),
                change.getTargetPrice()
        );
    }

    public ScheduledDishChange toDomain() {
        return new ScheduledDishChange(
                new ScheduledDishChangeId(id),
                new DishId(dishId),
                scheduledTime,
                targetState,
                targetName,
                targetDescription,
                targetPrice
        );
    }
}
