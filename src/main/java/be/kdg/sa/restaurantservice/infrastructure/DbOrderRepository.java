package be.kdg.sa.restaurantservice.infrastructure;

import be.kdg.sa.restaurantservice.domain.order.Order;
import be.kdg.sa.restaurantservice.domain.order.OrderRepository;
import be.kdg.sa.restaurantservice.infrastructure.jpa.JpaOrderEntity;
import be.kdg.sa.restaurantservice.infrastructure.jpa.JpaOrderRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class DbOrderRepository implements OrderRepository {
    private final JpaOrderRepository jpaOrderRepository;

    public DbOrderRepository(JpaOrderRepository jpaOrderRepository) {
        this.jpaOrderRepository = jpaOrderRepository;
    }


    @Override
    public void save(Order order) {
        this.jpaOrderRepository.save(JpaOrderEntity.fromDomain(order));
    }
}
