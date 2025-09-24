package be.kdg.sa.restaurantservice.api;

import be.kdg.sa.restaurantservice.application.CreateRestaurantCommand;
import be.kdg.sa.restaurantservice.application.RestaurantService;
import be.kdg.sa.restaurantservice.domain.Restaurant.Restaurant;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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


}
