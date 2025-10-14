package be.kdg.sa.restaurantservice.domain.order;

import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository {
    void save(Order order);
}
