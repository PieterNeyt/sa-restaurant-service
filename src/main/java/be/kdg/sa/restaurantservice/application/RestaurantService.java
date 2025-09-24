package be.kdg.sa.restaurantservice.application;

import be.kdg.sa.restaurantservice.api.RestaurantDto;
import be.kdg.sa.restaurantservice.domain.Address.AddressId;
import be.kdg.sa.restaurantservice.domain.Owner.OwnerId;
import be.kdg.sa.restaurantservice.domain.Restaurant.Dish;
import be.kdg.sa.restaurantservice.domain.Restaurant.Restaurant;
import be.kdg.sa.restaurantservice.domain.Restaurant.RestaurantRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import be.kdg.sa.restaurantservice.api.RestaurantDto.*;
import be.kdg.sa.restaurantservice.application.CreateRestaurantCommand.*;
@Service
@Transactional
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;

    public RestaurantService(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    public Restaurant createRestaurant(CreateRestaurantCommand restaurantCommand) {
        Restaurant restaurant = new Restaurant(
                new OwnerId(restaurantCommand.ownerId()),
                new AddressId(restaurantCommand.addressId()),restaurantCommand.restaurantType(),restaurantCommand.name(),
                restaurantCommand.email(),restaurantCommand.logo());
        restaurantRepository.save(restaurant);
        return restaurant;
    }

    public Dish createDish(CreateDishCommand command) {
        Dish dish = new Dish(command.name(),command.description(),command.price());
        Restaurant restaurant =  restaurantRepository.findById(command.restaurantId())
                .orElseThrow();
        restaurant.addDish(dish.getId().id(),dish.getDescription(),dish.getName(),dish.getState(),dish.getPrice());

        restaurantRepository.save(restaurant);
        return dish;
    }
}
