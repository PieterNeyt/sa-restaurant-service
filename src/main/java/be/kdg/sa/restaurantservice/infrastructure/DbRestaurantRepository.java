package be.kdg.sa.restaurantservice.infrastructure;

import be.kdg.sa.restaurantservice.domain.restaurant.Restaurant;
import be.kdg.sa.restaurantservice.domain.restaurant.RestaurantFactory;
import be.kdg.sa.restaurantservice.domain.restaurant.RestaurantRepository;
import be.kdg.sa.restaurantservice.infrastructure.restaurant.jpa.JpaRestaurantEntity;
import be.kdg.sa.restaurantservice.infrastructure.restaurant.jpa.JpaRestaurantRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class DbRestaurantRepository implements RestaurantRepository {
    private final JpaRestaurantRepository jpaRestaurantRepository;
    private final RestaurantFactory restaurantFactory;

    DbRestaurantRepository(JpaRestaurantRepository jpaRestaurantRepository, RestaurantFactory restaurantFactory) {
        this.jpaRestaurantRepository = jpaRestaurantRepository;
        this.restaurantFactory = restaurantFactory;
    }

    @Override
    public void save(Restaurant restaurant) {
        JpaRestaurantEntity jpaRestaurant =JpaRestaurantEntity.fromDomain(restaurant);
        this.jpaRestaurantRepository.save(jpaRestaurant);
    }

    @Override
    public Optional<Restaurant> findById(UUID id) {
        return this.jpaRestaurantRepository.findById(id).map(entity -> entity.toDomain(restaurantFactory));
    }

    @Override
    public boolean CheckIfOwnerAlreadyOwnsRestaurant(UUID ownerId) {
        return jpaRestaurantRepository.findByOwnerId(ownerId).isPresent();
    }

    @Override
    public Optional<Restaurant> findRestaurantFromDishId(UUID id) {
        return this.jpaRestaurantRepository.findRestaurantIdByDishId(id).map(entity -> entity.toDomain(restaurantFactory));
    }

    @Override
    public List<Restaurant> findAll() {
        return jpaRestaurantRepository.findAll().stream().map(entity -> entity.toDomain(restaurantFactory)).toList();
    }

}
