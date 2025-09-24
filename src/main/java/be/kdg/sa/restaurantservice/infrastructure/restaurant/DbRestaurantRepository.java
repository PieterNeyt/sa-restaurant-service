package be.kdg.sa.restaurantservice.infrastructure.restaurant;

import be.kdg.sa.restaurantservice.domain.Restaurant.Dish;
import be.kdg.sa.restaurantservice.domain.Restaurant.DishState;
import be.kdg.sa.restaurantservice.domain.Restaurant.Restaurant;
import be.kdg.sa.restaurantservice.domain.Restaurant.RestaurantRepository;
import be.kdg.sa.restaurantservice.infrastructure.restaurant.jpa.JpaDishEntity;
import be.kdg.sa.restaurantservice.infrastructure.restaurant.jpa.JpaRestaurantEntity;
import be.kdg.sa.restaurantservice.infrastructure.restaurant.jpa.JpaRestaurantRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class DbRestaurantRepository implements RestaurantRepository {
    private final JpaRestaurantRepository jpaRestaurantRepository;

    DbRestaurantRepository(JpaRestaurantRepository jpaRestaurantRepository) {
        this.jpaRestaurantRepository = jpaRestaurantRepository;
    }

    @Override
    public void save(Restaurant restaurant) {
        JpaRestaurantEntity jpaRestaurant =JpaRestaurantEntity.fromDomain(restaurant);
        JpaRestaurantEntity savedEntity = this.jpaRestaurantRepository.save(jpaRestaurant);
        //checken dat het wel degelijk gesaved is
        if (savedEntity.getId() == null) {
            throw new RuntimeException("Failed to save restaurant");
        }
    }

    @Override
    public Optional<Restaurant> findById(UUID id) {
        return this.jpaRestaurantRepository.findById(id).map(JpaRestaurantEntity::toDomain);
    }

    @Override
    public boolean CheckIfOwnerAlreadyOwnsRestaurant(UUID ownerId) {
        return jpaRestaurantRepository.findByOwnerId(ownerId).isPresent();
    }

    @Override
    public Optional<Restaurant> findRestaurantFromDishId(UUID id) {
        return this.jpaRestaurantRepository.findRestaurantIdByDishId(id).map(JpaRestaurantEntity::toDomain);
    }
}
