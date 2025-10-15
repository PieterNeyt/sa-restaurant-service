package be.kdg.sa.restaurantservice.infrastructure;

import be.kdg.sa.restaurantservice.domain.order.Order;
import be.kdg.sa.restaurantservice.domain.order.OrderRepository;
import be.kdg.sa.restaurantservice.infrastructure.jpa.JpaOrderEntity;
import be.kdg.sa.restaurantservice.infrastructure.jpa.JpaOrderRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
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

    @Override
    public List<Order> findOrdersByRestaurantId(UUID restaurantId) {
        return this.jpaOrderRepository.findOrdersByRestaurantId(restaurantId)
                .stream().map(JpaOrderEntity::toDomain).toList();
    }

    @Override
    public Optional<Order> findByid(UUID orderId) {
        return this.jpaOrderRepository.findById(orderId).map(JpaOrderEntity::toDomain);
    }

    @Override
    public void delete(Order order) {
        this.jpaOrderRepository.delete(JpaOrderEntity.fromDomain(order));
    }

    @Override
    public List<Order> findAllPendingOrders() {
        return this.jpaOrderRepository.findAllPendingOrders().stream()
                .map(JpaOrderEntity::toDomain)
                .toList();
    }
}
