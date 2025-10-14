package be.kdg.sa.restaurantservice;

import be.kdg.sa.restaurantservice.domain.restaurant.Restaurant;
import be.kdg.sa.restaurantservice.domain.restaurant.RestaurantType;
import be.kdg.sa.restaurantservice.infrastructure.jpa.JpaRestaurantEntity;
import be.kdg.sa.restaurantservice.infrastructure.jpa.JpaRestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.util.UUID;

@Component
public class TestHelper {
    @Autowired
    private JpaRestaurantRepository jpaRestaurantRepository;

    public Restaurant saveRestaurent() {
        JpaRestaurantEntity restaurantEntity = new JpaRestaurantEntity(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(),"dominos","dominos@mail.com","logo.png", RestaurantType.PIZZERIA);
        jpaRestaurantRepository.save(restaurantEntity);
        return restaurantEntity.toDomain();
    }

    public void cleanUp() {
        jpaRestaurantRepository.deleteAll();
    }
}
