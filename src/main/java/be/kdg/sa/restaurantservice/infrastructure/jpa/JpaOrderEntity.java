package be.kdg.sa.restaurantservice.infrastructure.jpa;

import be.kdg.sa.restaurantservice.domain.order.Order;
import be.kdg.sa.restaurantservice.domain.order.OrderId;
import be.kdg.sa.restaurantservice.domain.restaurant.RestaurantId;
import jakarta.persistence.*;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Entity
@Getter
@Table(name = "orders")
public class JpaOrderEntity {
    @Id
    private UUID id;
    @Column(nullable = false)
    private UUID restaurantId;
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal price;
    @Column(nullable = false, precision = 19, scale = 2)
    private Date startDate;

    @Column()
    private String message;
    @Column()
    private boolean isAccepted;

    public JpaOrderEntity() {
    }

    public JpaOrderEntity(UUID id, UUID restaurantId, BigDecimal price, Date startDate, String message, boolean isAccepted) {
        this.id = id;
        this.restaurantId = restaurantId;
        this.price = price;
        this.startDate = startDate;
        this.message = message;
        this.isAccepted = isAccepted;
    }

    public static JpaOrderEntity fromDomain(Order order) {
        return new JpaOrderEntity(order.getOrderId().id(),
                order.getRestaurantId().id(),
                order.getPrice(),
                order.getStartDate(),
                order.getMessage(),
                order.isAccepted());
    }

    public Order toDomain() {
        return new Order(
                new OrderId(id),
               new RestaurantId(restaurantId),
                price,
                startDate,
                message,
                isAccepted);
    }
}
