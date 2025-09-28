package be.kdg.sa.restaurantservice.api;

import be.kdg.sa.restaurantservice.api.RestaurantDto.DishDto;
import be.kdg.sa.restaurantservice.api.RestaurantDto.RestaurantChangesOverviewDto;
import be.kdg.sa.restaurantservice.api.RestaurantDto.ScheduleDishChangeDto;
import be.kdg.sa.restaurantservice.application.CreateDishCommand;
import be.kdg.sa.restaurantservice.application.CreateRestaurantCommand;
import be.kdg.sa.restaurantservice.application.RestaurantService;
import be.kdg.sa.restaurantservice.application.ScheduledDishChangeService;
import be.kdg.sa.restaurantservice.domain.Restaurant.Dish;
import be.kdg.sa.restaurantservice.domain.Restaurant.DishState;
import be.kdg.sa.restaurantservice.domain.Restaurant.Restaurant;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/restaurant")
public class RestaurantController {
    private final RestaurantService restaurantService;
    private final ScheduledDishChangeService scheduledDishChangeService;

    public RestaurantController(RestaurantService restaurantService, ScheduledDishChangeService scheduledDishChangeService) {
        this.restaurantService = restaurantService;
        this.scheduledDishChangeService = scheduledDishChangeService;
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
    public ResponseEntity<Void> scheduleDishChange(@RequestBody ScheduleDishChangeDto request) {
        scheduledDishChangeService.scheduleDishChange(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/applyAllScheduledChangesForRestaurant")
    public ResponseEntity<Void> applyAllScheduledChanges(
            @RequestParam UUID ownerId,
            @RequestParam UUID restaurantId) {
        scheduledDishChangeService.applyAllPendingChanges(ownerId, restaurantId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{restaurantId}/changesOverview")
    public ResponseEntity<RestaurantChangesOverviewDto> getRestaurantChangesOverview(
            @PathVariable UUID restaurantId,
            @RequestParam UUID ownerId
    ) {
        var overview = restaurantService.getOverviewForRestaurantAndOwner(restaurantId, ownerId);
        return ResponseEntity.ok(overview);
    }






}
