package be.kdg.sa.restaurantservice.application;

import be.kdg.sa.restaurantservice.api.RestaurantDto;
import be.kdg.sa.restaurantservice.domain.address.AddressId;
import be.kdg.sa.restaurantservice.domain.owner.OwnerId;
import be.kdg.sa.restaurantservice.domain.restaurant.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
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
        restaurant.updateDishState(dishId,state);
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

    public OpeningHour addOpenhours(UUID restaurantId, LocalTime closingTime, LocalTime openingTime, DayOfWeek dayOfWeek) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(() -> new IllegalArgumentException("Restaurant not found"));
        OpeningHour openingHour = new OpeningHour(dayOfWeek,openingTime,closingTime);
        restaurant.addOpeningHour(openingHour);
        restaurantRepository.save(restaurant);
        return openingHour;
    }

    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAll();
    }

    public Restaurant getRestaurantById(UUID restaurantId) {
        return restaurantRepository.findById(restaurantId).orElseThrow(() -> new IllegalArgumentException("Restaurant not found"));
    }
}
