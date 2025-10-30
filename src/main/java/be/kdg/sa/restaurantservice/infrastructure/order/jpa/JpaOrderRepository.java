package be.kdg.sa.restaurantservice.infrastructure.order.jpa;

import be.kdg.sa.restaurantservice.domain.order.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface JpaOrderRepository extends JpaRepository<JpaOrderEntity, UUID> {
    @Query("SELECT jpo FROM JpaOrderEntity jpo WHERE jpo.restaurantId = :restaurantId AND jpo.status = 'PENDING'")
    List<JpaOrderEntity> findOrdersByRestaurantId(@Param("restaurantId") UUID restaurantId);

    Collection<JpaOrderEntity> findByStatus(OrderStatus status);
}
