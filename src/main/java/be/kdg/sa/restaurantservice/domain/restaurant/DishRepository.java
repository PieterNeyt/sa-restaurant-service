package be.kdg.sa.restaurantservice.domain.restaurant;


import java.util.Optional;


public interface DishRepository {
    void save(Dish dish);
    Optional<Dish> findById(DishId id);
}
