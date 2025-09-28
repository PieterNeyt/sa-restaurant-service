package be.kdg.sa.restaurantservice.domain.Restaurant;



import org.springframework.stereotype.Repository;

import java.util.Optional;


public interface DishRepository {
    void save(Dish dish);
    Optional<Dish> findById(DishId id);
}
