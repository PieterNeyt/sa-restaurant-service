package be.kdg.sa.restaurantservice.domain.order;

import be.kdg.sa.restaurantservice.domain.restaurant.RestaurantId;
import lombok.Getter;
import org.jmolecules.ddd.annotation.Entity;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Entity
@Getter
public class Order {
    private final OrderId orderId;
    private final RestaurantId restaurantId;
    private final BigDecimal price;
    private final Date startDate;
    private String message;
    private boolean accepted;

    public Order(OrderId orderId, RestaurantId restaurantId, BigDecimal price) {
        this.orderId = orderId;
        this.restaurantId = restaurantId;
        this.price = price;
        this.accepted = false;
        this.startDate = Date.from(Instant.now());
    }

    public Order(OrderId orderId, RestaurantId restaurantId, BigDecimal price, Date setDate, String message, boolean accepted) {
        this.orderId = orderId;
        this.restaurantId = restaurantId;
        this.price = price;
        this.startDate = setDate;
        this.message = message;
        this.accepted = accepted;
    }

    public void setMessage(String message) {
        Assert.hasText(message, "message must not be empty");
        this.message = message;
    }

    public void accept() {
        if(accepted)
            throw new IllegalStateException("Order has already been accepted");

        this.accepted = true;
    }

    public void deny(String message) {
        if(accepted)
            throw new IllegalStateException("Order has already been accepted");

        setMessage(message);
        this.accepted = false;
    }

    public void checkRestaurant(UUID restaurantId) {
        if(!restaurantId.equals(this.restaurantId.id())) {
            throw new RuntimeException("this order does not belong to restaurant");
        }
    }
}
