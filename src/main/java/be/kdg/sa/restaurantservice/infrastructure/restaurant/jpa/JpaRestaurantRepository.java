package be.kdg.sa.restaurantservice.infrastructure.restaurant.jpa;


import io.micrometer.observation.ObservationFilter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;


public interface JpaRestaurantRepository extends JpaRepository<JpaRestaurantEntity, UUID> {
    Optional<JpaRestaurantEntity> findByOwnerId(UUID ownerId);

    @Query("SELECT r FROM JpaRestaurantEntity r JOIN r.dishes d WHERE d.id = :dishId")
    Optional<JpaRestaurantEntity> findRestaurantIdByDishId(@Param("dishId") UUID dishId);

}
