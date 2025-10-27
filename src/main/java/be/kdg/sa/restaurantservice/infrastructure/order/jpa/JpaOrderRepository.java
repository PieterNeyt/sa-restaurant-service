package be.kdg.sa.restaurantservice.infrastructure.order.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface JpaOrderRepository extends JpaRepository<JpaOrderEntity, UUID> {
    @Query("SELECT jpo FROM JpaOrderEntity jpo WHERE jpo.restaurantId = :restaurantId AND jpo.status = 'PENDING'")
    List<JpaOrderEntity> findOrdersByRestaurantId(@Param("restaurantId") UUID restaurantId);

    @Query("SELECT jpo FROM JpaOrderEntity jpo WHERE jpo.status = 'PENDING'")
    List<JpaOrderEntity> findAllPendingOrders();
}
