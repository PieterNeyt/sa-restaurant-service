package be.kdg.sa.restaurantservice.application;

import be.kdg.sa.restaurantservice.api.RestaurantDto;
import be.kdg.sa.restaurantservice.domain.Address.AddressId;
import be.kdg.sa.restaurantservice.domain.Owner.OwnerId;
import be.kdg.sa.restaurantservice.domain.Restaurant.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;
    private final ScheduledDishChangeRepository scheduledRepo;

    public RestaurantService(RestaurantRepository restaurantRepository, ScheduledDishChangeRepository scheduledRepo) {
        this.restaurantRepository = restaurantRepository;
        this.scheduledRepo = scheduledRepo;
    }

    public Restaurant createRestaurant(CreateRestaurantCommand restaurantCommand) {
        //check of owner al een restaurant heeft anders throw exception
        if (restaurantRepository.CheckIfOwnerAlreadyOwnsRestaurant(restaurantCommand.ownerId()))
            throw new IllegalStateException("Owner already owns restaurant");

        //maak nieuw restaurant aan
        Restaurant restaurant = new Restaurant(
                new OwnerId(restaurantCommand.ownerId()),
                new AddressId(restaurantCommand.addressId()),restaurantCommand.restaurantType(),restaurantCommand.name(),
                restaurantCommand.email(),restaurantCommand.logo());

        //slaag deze op
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

    public void updateStateDish(UUID dishId, DishState state) {
        Restaurant restaurant = restaurantRepository.findRestaurantFromDishId(dishId).orElseThrow();
        restaurant.updateDish(dishId,state);
        restaurantRepository.save(restaurant);
    }
    public void updateOpenState(UUID restaurantId, UUID requesterId ) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow();
        restaurant.changeOpenState(requesterId);
        restaurantRepository.save(restaurant);
    }

    public RestaurantDto.RestaurantChangesOverviewDto getOverviewForRestaurantAndOwner(UUID restaurantId, UUID ownerId) {
        var restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new IllegalArgumentException("Restaurant not found"));

        if (!restaurant.getOwnerId().id().equals(ownerId)) {
            throw new SecurityException("Not allowed to view changes for this restaurant");
        }

        // pending changes ophalen
        var pendingChanges = scheduledRepo.findDueChangesByRestaurantAndOwner(restaurantId, ownerId);

        return RestaurantDto.RestaurantChangesOverviewDto.from(restaurant, pendingChanges);
    }

}
