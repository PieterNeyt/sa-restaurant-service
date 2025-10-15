package be.kdg.sa.restaurantservice.infrastructure.jpa;

import be.kdg.sa.restaurantservice.domain.order.Order;
import be.kdg.sa.restaurantservice.domain.order.OrderId;
import be.kdg.sa.restaurantservice.domain.order.OrderStatus;
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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    public JpaOrderEntity() {
    }

    public JpaOrderEntity(UUID id, UUID restaurantId, BigDecimal price, Date startDate, String message, OrderStatus status) {
        this.id = id;
        this.restaurantId = restaurantId;
        this.price = price;
        this.startDate = startDate;
        this.message = message;
        this.status = status;
    }

    public static JpaOrderEntity fromDomain(Order order) {
        return new JpaOrderEntity(order.getOrderId().id(),
                order.getRestaurantId().id(),
                order.getPrice(),
                order.getStartDate(),
                order.getMessage(),
                order.getStatus());
    }

    public Order toDomain() {
        return new Order(
                new OrderId(id),
               new RestaurantId(restaurantId),
                price,
                startDate,
                message,
                status);
    }
}
