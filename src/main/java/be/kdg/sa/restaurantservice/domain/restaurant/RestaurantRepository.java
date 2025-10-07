package be.kdg.sa.restaurantservice.domain.restaurant;

import org.jmolecules.ddd.annotation.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RestaurantRepository {
    void save(Restaurant restaurant);

    Optional<Restaurant> findById(UUID id);

    boolean CheckIfOwnerAlreadyOwnsRestaurant(UUID ownerId);

    Optional<Restaurant> findRestaurantFromDishId(UUID id);

    List<Restaurant> findAll();

}
