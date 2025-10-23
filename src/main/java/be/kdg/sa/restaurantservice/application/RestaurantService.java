package be.kdg.sa.restaurantservice.application;

import be.kdg.sa.restaurantservice.application.command.CreateDishCommand;
import be.kdg.sa.restaurantservice.application.command.CreateRestaurantCommand;
import be.kdg.sa.restaurantservice.application.command.RestaurantChangesOverviewCommand;
import be.kdg.sa.restaurantservice.domain.NotFoundException;
import be.kdg.sa.restaurantservice.domain.address.Address;
import be.kdg.sa.restaurantservice.domain.owner.OwnerId;
import be.kdg.sa.restaurantservice.domain.restaurant.*;
import be.kdg.sa.restaurantservice.domain.restaurant.dish.Dish;
import be.kdg.sa.restaurantservice.domain.restaurant.dish.DishState;
import be.kdg.sa.restaurantservice.domain.schedulechange.ScheduledDishChangeRepository;
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
        if (restaurantRepository.CheckIfOwnerAlreadyOwnsRestaurant(restaurantCommand.ownerId()))
            throw new IllegalStateException("Owner already owns restaurant");

        Address address = new Address(
                restaurantCommand.city(),
                restaurantCommand.streetNumber(),
                restaurantCommand.street(),
                restaurantCommand.postalCode(),
                restaurantCommand.country()
        );

        Restaurant restaurant = new Restaurant(
                new OwnerId(restaurantCommand.ownerId()),
                address,
                restaurantCommand.restaurantType(),
                restaurantCommand.name(),
                restaurantCommand.email(),
                restaurantCommand.logo());

        restaurantRepository.save(restaurant);
        return restaurant;
    }

    public Dish createDish(CreateDishCommand command) {
        Dish dish = new Dish(command.name(),command.description(),command.price(),command.preparationTime(), command.dishState());

        Restaurant restaurant =  restaurantRepository.findById(command.restaurantId())
                .orElseThrow(() -> new NotFoundException("Restaurant not found"));

        restaurant.addDish(
                dish.getId().id(),
                dish.getDescription(),
                dish.getName(),
                dish.getState(),
                dish.getPrice(),
                dish.getPreparationTime());

        restaurantRepository.save(restaurant);
        return dish;
    }

    public void updateStateDish(UUID dishId, DishState state) {
        Restaurant restaurant = restaurantRepository.findRestaurantFromDishId(dishId)
                .orElseThrow(() -> new NotFoundException("Restaurant not found"));

        restaurant.updateDishState(dishId,state);
        restaurantRepository.save(restaurant);
    }
    public void updateOpenState(UUID restaurantId, UUID requesterId ) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new NotFoundException("Restaurant not found"));

        restaurant.changeOpenState(requesterId);
        restaurantRepository.save(restaurant);
    }

    public RestaurantChangesOverviewCommand getOverviewForRestaurantAndOwner(UUID restaurantId, UUID ownerId) {
        var restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new NotFoundException("Restaurant not found"));

        restaurant.isOwner(ownerId);
        var pendingChanges = scheduledRepo.findDueChangesByRestaurantAndOwner(restaurantId, ownerId);

        return RestaurantChangesOverviewCommand.from(restaurant, pendingChanges);
    }

    public OpeningHour addOpenhours(UUID restaurantId, LocalTime closingTime, LocalTime openingTime, DayOfWeek dayOfWeek) {
        Restaurant restaurant = restaurantRepository
                .findById(restaurantId)
                .orElseThrow(() -> new IllegalArgumentException("Restaurant not found"));

        OpeningHour openingHour = restaurant.addOpeningHour(dayOfWeek,openingTime,closingTime);
        restaurantRepository.save(restaurant);
        return openingHour;
    }

    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAll();
    }

    public Restaurant getRestaurantById(UUID restaurantId) {
        return restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new NotFoundException("Restaurant not found"));
    }

    public Restaurant GetRestaurantWithDishFromDish(UUID id) {
        return restaurantRepository.findRestaurantFromDishId(id)
                .orElseThrow(() -> new NotFoundException("Restaurant not found"));
    }

    public Dish getDishFromDishId(UUID id) {
        Restaurant restaurant = restaurantRepository.findRestaurantFromDishId(id)
                .orElseThrow(() -> new NotFoundException("Restaurant not found"));

        return restaurant.getDishes()
                .stream()
                .filter(d -> d.getId().id().equals(id))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Dish not found"));

    }
}
