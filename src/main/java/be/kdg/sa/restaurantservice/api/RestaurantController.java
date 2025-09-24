package be.kdg.sa.restaurantservice.api;

import be.kdg.sa.restaurantservice.application.CreateRestaurantCommand;
import be.kdg.sa.restaurantservice.application.RestaurantService;
import be.kdg.sa.restaurantservice.domain.Restaurant.Dish;
import be.kdg.sa.restaurantservice.domain.Restaurant.Restaurant;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import be.kdg.sa.restaurantservice.api.RestaurantDto.*;
import be.kdg.sa.restaurantservice.application.CreateRestaurantCommand.*;

@RestController
@RequestMapping("/api/restaurant")
public class RestaurantController {
    private final RestaurantService restaurantService;
    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @PostMapping("/addRestaurant")
    public ResponseEntity<RestaurantDto> addRestaurant(@RequestBody RestaurantDto restaurantDto) {
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


}
