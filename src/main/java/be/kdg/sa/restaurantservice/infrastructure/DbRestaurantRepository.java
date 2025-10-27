package be.kdg.sa.restaurantservice.infrastructure;

import be.kdg.sa.restaurantservice.domain.restaurant.Restaurant;
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

    DbRestaurantRepository(JpaRestaurantRepository jpaRestaurantRepository) {
        this.jpaRestaurantRepository = jpaRestaurantRepository;
    }

    @Override
    public void save(Restaurant restaurant) {
        JpaRestaurantEntity jpaRestaurant =JpaRestaurantEntity.fromDomain(restaurant);
        this.jpaRestaurantRepository.save(jpaRestaurant);
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

    @Override
    public List<Restaurant> findAll() {
        return jpaRestaurantRepository.findAll().stream().map(JpaRestaurantEntity::toDomain).toList();
    }

}
