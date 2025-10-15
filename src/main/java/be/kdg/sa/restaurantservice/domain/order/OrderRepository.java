package be.kdg.sa.restaurantservice.domain.order;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository {
    void save(Order order);

   List<Order> findOrdersByRestaurantId(UUID restaurantId);

    Optional<Order> findByid(UUID orderId);

    void delete(Order order);
}
