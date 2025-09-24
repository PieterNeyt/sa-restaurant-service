package be.kdg.sa.restaurantservice.infrastructure.restaurant;

import be.kdg.sa.restaurantservice.domain.Restaurant.Restaurant;
import be.kdg.sa.restaurantservice.domain.Restaurant.RestaurantRepository;
import be.kdg.sa.restaurantservice.infrastructure.restaurant.jpa.JpaRestaurantEntity;
import be.kdg.sa.restaurantservice.infrastructure.restaurant.jpa.JpaRestaurantRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class DbRestaurantRepository implements RestaurantRepository {
    private final JpaRestaurantRepository jpaRestaurantRepository;

    DbRestaurantRepository(JpaRestaurantRepository jpaRestaurantRepository) {
        this.jpaRestaurantRepository = jpaRestaurantRepository;
    }

    @Override
    public void save(Restaurant restaurant) {
        JpaRestaurantEntity jpaRestaurant =JpaRestaurantEntity.fromDomain(restaurant);
        this.jpaRestaurantRepository.saveAndFlush(jpaRestaurant);
        System.out.println("jeej");
    }

}
