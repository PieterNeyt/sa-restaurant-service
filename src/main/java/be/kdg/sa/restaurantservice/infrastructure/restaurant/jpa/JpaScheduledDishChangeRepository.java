package be.kdg.sa.restaurantservice.infrastructure.restaurant.jpa;

import org.jmolecules.ddd.annotation.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface JpaScheduledDishChangeRepository extends JpaRepository<JpaScheduledDishChangeEntity, UUID> {
    List<JpaScheduledDishChangeEntity> findByScheduledTimeBefore(LocalDateTime now);

    List<JpaScheduledDishChangeEntity> findByScheduledTimeAfter(LocalDateTime now);


    @Query("SELECT sdc FROM JpaScheduledDishChangeEntity sdc " +
            "JOIN JpaDishEntity d ON sdc.dishId = d.id " +
            "WHERE d.restaurant.id = :restaurantId " +
            "AND d.restaurant.ownerId = :ownerId " )
    List<JpaScheduledDishChangeEntity> findDueChangesForRestaurantAndOwner(
            @Param("restaurantId") UUID restaurantId,
            @Param("ownerId") UUID ownerId);
}