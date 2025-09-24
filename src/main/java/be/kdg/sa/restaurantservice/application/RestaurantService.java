package be.kdg.sa.restaurantservice.application;

import be.kdg.sa.restaurantservice.domain.Address.AddressId;
import be.kdg.sa.restaurantservice.domain.Owner.OwnerId;
import be.kdg.sa.restaurantservice.domain.Restaurant.Restaurant;
import be.kdg.sa.restaurantservice.domain.Restaurant.RestaurantRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
