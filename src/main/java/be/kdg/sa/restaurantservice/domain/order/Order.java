package be.kdg.sa.restaurantservice.domain.order;

import be.kdg.sa.restaurantservice.domain.restaurant.RestaurantId;
import lombok.Getter;
import org.jmolecules.ddd.annotation.Entity;
import org.jmolecules.ddd.annotation.Identity;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Entity
@Getter
public class Order {
    @Identity
    private final OrderId orderId;
    private final RestaurantId restaurantId;
    private final BigDecimal price;
    private final Date startDate;
    private String message;
    private OrderStatus status;

    public Order(OrderId orderId, RestaurantId restaurantId, BigDecimal price) {
        this.orderId = orderId;
        this.restaurantId = restaurantId;
        this.price = price;
        this.status = OrderStatus.PENDING;
        this.startDate = Date.from(Instant.now());
    }

    public Order(OrderId orderId, RestaurantId restaurantId, BigDecimal price, Date setDate, String message, OrderStatus status) {
        this.orderId = orderId;
        this.restaurantId = restaurantId;
        this.price = price;
        this.startDate = setDate;
        this.message = message;
        this.status = status;
    }

    public void setMessage(String message) {
        Assert.hasText(message, "message must not be empty");
        this.message = message;
    }

    public void accept(UUID restaurantId) {
        checkRestaurant(restaurantId);

        if(status == OrderStatus.ACCEPTED || status == OrderStatus.READY_FOR_PICKUP)
            throw new IllegalStateException("Order has already been accepted or is altready ready for pick up");

        this.status = OrderStatus.ACCEPTED;
    }

    public void deny(String message, UUID restaurantId) {
        checkRestaurant(restaurantId);

        if( status == OrderStatus.READY_FOR_PICKUP)
            throw new IllegalStateException("Order is already ready for pick up");

        setMessage(message);
        this.status = OrderStatus.DENIED;
    }

    public void ready(UUID restaurantId) {
        checkRestaurant(restaurantId);

        if(status != OrderStatus.ACCEPTED)
            throw new IllegalStateException("Order is not yet accepted");

        this.status = OrderStatus.READY_FOR_PICKUP;
    }

    public void checkRestaurant(UUID restaurantId) {
        if(!restaurantId.equals(this.restaurantId.id())) {
            throw new RuntimeException("this order does not belong to restaurant");
        }
    }

    public boolean has5minutsPassed() {
        long currentTime = new Date().getTime();
        long elapsedTime = currentTime - startDate.getTime();
        return elapsedTime >= 5 * 60 * 1000;
    }
}
