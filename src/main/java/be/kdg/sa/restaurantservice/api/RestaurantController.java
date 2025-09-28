package be.kdg.sa.restaurantservice.api;

import be.kdg.sa.restaurantservice.application.CreateRestaurantCommand;
import be.kdg.sa.restaurantservice.application.RestaurantService;
import be.kdg.sa.restaurantservice.domain.Restaurant.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import be.kdg.sa.restaurantservice.api.RestaurantDto.*;
import be.kdg.sa.restaurantservice.application.CreateRestaurantCommand.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/restaurant")
public class RestaurantController {
    private final RestaurantService restaurantService;
    private final ScheduledDishChangeRepository scheduledDishChangeRepository;

    public RestaurantController(RestaurantService restaurantService, ScheduledDishChangeRepository scheduledDishChangeRepository) {
        this.restaurantService = restaurantService;
        this.scheduledDishChangeRepository = scheduledDishChangeRepository;
    }




    @PostMapping("/addRestaurant")
    public ResponseEntity<RestaurantDto> addRestaurant(@RequestBody RestaurantDto restaurantDto) {
        //zet het om naar command om minder parameters in methode te hebben
        CreateRestaurantCommand command = new CreateRestaurantCommand(
                restaurantDto.ownerId(),
                restaurantDto.addressId(),
                restaurantDto.restaurantType(),
                restaurantDto.name(),
                restaurantDto.email(),
                restaurantDto.logo(),
                restaurantDto.dishes()
        );

        Restaurant restaurant = restaurantService.createRestaurant(command);
        return ResponseEntity.ok(RestaurantDto.from(restaurant));
    }

    @PostMapping("/addDish")
    public ResponseEntity<RestaurantDto.DishDto> addDish(@RequestBody DishDto dishDto) {
        CreateDishCommand command = new CreateDishCommand(
                dishDto.RestaurantId(),
                dishDto.name(),
                dishDto.description(),
                dishDto.price(),
                DishState.NOT_PUBLISHED
        );
        Dish dish = restaurantService.createDish(command);
        return ResponseEntity.ok(DishDto.from(dish,dishDto.RestaurantId()));
    }

    @PutMapping("/changeStateDish/{id}")
    public ResponseEntity<Void> changeStateDish(@PathVariable("id") UUID id,
                                                         @RequestBody DishState state) {
        restaurantService.updateStateDish(id,state);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/changeOpenState")
    public ResponseEntity<Void> changeOpenState(
            @PathVariable("id") UUID restaurantId,
            @RequestParam("ownerId") UUID ownerId
    ) {
        restaurantService.updateOpenState(restaurantId, ownerId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/scheduleDishChange")
    public ResponseEntity<Void> scheduleDishChange(@RequestBody ScheduleDishChangeRequest request) {
        ScheduledDishChange change = new ScheduledDishChange(
                new DishId(request.dishId()),
                request.scheduledTime(),
                request.targetState()
        );
        scheduledDishChangeRepository.save(change);
        return ResponseEntity.ok().build();
    }




}
