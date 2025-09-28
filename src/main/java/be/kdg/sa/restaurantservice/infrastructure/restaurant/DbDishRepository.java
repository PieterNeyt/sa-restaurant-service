package be.kdg.sa.restaurantservice.infrastructure.restaurant;

import be.kdg.sa.restaurantservice.domain.Restaurant.Dish;
import be.kdg.sa.restaurantservice.domain.Restaurant.DishId;
import be.kdg.sa.restaurantservice.domain.Restaurant.DishRepository;
import be.kdg.sa.restaurantservice.infrastructure.restaurant.jpa.JpaDishEntity;
import be.kdg.sa.restaurantservice.infrastructure.restaurant.jpa.JpaDishRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;
import java.util.UUID;

@Repository
public class DbDishRepository implements DishRepository {
    private final JpaDishRepository jpa;

    public DbDishRepository(JpaDishRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public void save(Dish dish) {
        UUID id = dish.getId().id();
        Optional<JpaDishEntity> existing = jpa.findById(id);

        if (existing.isPresent()) {
            // update de velden die veranderen
            JpaDishEntity entity = existing.get();
            entity.setState(dish.getState());
            entity.setName(dish.getName());
            entity.setDescription(dish.getDescription());
            entity.setPrice(dish.getPrice());
            jpa.save(entity);
        } else {
            jpa.save(JpaDishEntity.fromDomain(dish));
        }
    }

    @Override
    public Optional<Dish> findById(DishId id) {
        return jpa.findById(id.id()).map(JpaDishEntity::toDomain);
    }
}