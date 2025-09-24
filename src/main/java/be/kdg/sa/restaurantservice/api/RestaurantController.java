package be.kdg.sa.restaurantservice.api;

import be.kdg.sa.restaurantservice.application.CreateRestaurantCommand;
import be.kdg.sa.restaurantservice.application.RestaurantService;
import be.kdg.sa.restaurantservice.domain.Restaurant.Dish;
import be.kdg.sa.restaurantservice.domain.Restaurant.DishState;
import be.kdg.sa.restaurantservice.domain.Restaurant.Restaurant;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import be.kdg.sa.restaurantservice.api.RestaurantDto.*;
import be.kdg.sa.restaurantservice.application.CreateRestaurantCommand.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/restaurant")
public class RestaurantController {
    private final RestaurantService restaurantService;
    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
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
                dishDto.dishState()
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


}
