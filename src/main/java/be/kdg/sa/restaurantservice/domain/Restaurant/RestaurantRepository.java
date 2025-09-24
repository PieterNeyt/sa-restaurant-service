package be.kdg.sa.restaurantservice.domain.Restaurant;

import org.jmolecules.ddd.annotation.Repository;

@Repository
public interface RestaurantRepository {
    void save(Restaurant restaurant);
}
