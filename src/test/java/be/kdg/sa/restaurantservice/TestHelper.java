package be.kdg.sa.restaurantservice;

import be.kdg.sa.restaurantservice.domain.restaurant.PriceCategory;
import be.kdg.sa.restaurantservice.domain.restaurant.Restaurant;
import be.kdg.sa.restaurantservice.domain.restaurant.RestaurantType;
import be.kdg.sa.restaurantservice.infrastructure.restaurant.jpa.JpaAddressEmbeddable;
import be.kdg.sa.restaurantservice.infrastructure.restaurant.jpa.JpaRestaurantEntity;
import be.kdg.sa.restaurantservice.infrastructure.restaurant.jpa.JpaRestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.util.UUID;

@Component
public class TestHelper {
    @Autowired
    private JpaRestaurantRepository jpaRestaurantRepository;

    public Restaurant saveRestaurent() {
        JpaAddressEmbeddable address = new JpaAddressEmbeddable(
                "Groenplaats",
                "1",
                "Antwerpen",
                "2000",
                "Belgium"
        );


        JpaRestaurantEntity restaurantEntity = new JpaRestaurantEntity(
                UUID.randomUUID(),
                UUID.randomUUID(),
                address,
                "dominos",
                "dominos@mail.com",
                "logo.png",
                RestaurantType.PIZZERIA,
                PriceCategory.NORMAL
        );

        jpaRestaurantRepository.save(restaurantEntity);
        return restaurantEntity.toDomain();
    }

    public void cleanUp() {
        jpaRestaurantRepository.deleteAll();
    }
}
