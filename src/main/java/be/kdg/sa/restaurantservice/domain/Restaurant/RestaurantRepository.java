package be.kdg.sa.restaurantservice.domain.Restaurant;

import org.jmolecules.ddd.annotation.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RestaurantRepository {
    void save(Restaurant restaurant);

    Optional<Restaurant> findById(UUID id);
}
