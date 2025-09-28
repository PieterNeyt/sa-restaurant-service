package be.kdg.sa.restaurantservice.infrastructure.restaurant.jpa;

import org.jmolecules.ddd.annotation.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface JpaScheduledDishChangeRepository extends JpaRepository<JpaScheduledDishChangeEntity, UUID> {
    List<JpaScheduledDishChangeEntity> findByScheduledTimeBefore(LocalDateTime now);
}