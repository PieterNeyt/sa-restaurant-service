package be.kdg.sa.restaurantservice.api;

import be.kdg.sa.restaurantservice.api.RestaurantDto.DishDto;
import be.kdg.sa.restaurantservice.api.RestaurantDto.RestaurantChangesOverviewDto;
import be.kdg.sa.restaurantservice.api.RestaurantDto.ScheduleDishChangeDto;
import be.kdg.sa.restaurantservice.application.*;
import be.kdg.sa.restaurantservice.domain.NotFoundException;
import be.kdg.sa.restaurantservice.domain.restaurant.Dish;
import be.kdg.sa.restaurantservice.domain.restaurant.DishState;
import be.kdg.sa.restaurantservice.domain.restaurant.Restaurant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/restaurant")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:9090"})
public class RestaurantController {
    private final RestaurantService restaurantService;
    private final ScheduledRestaurantChangeService scheduledDishChangeService;
    private final CheckoutService checkoutService;

    public RestaurantController(RestaurantService restaurantService, ScheduledRestaurantChangeService scheduledDishChangeService, CheckoutService checkoutService) {
        this.restaurantService = restaurantService;
        this.scheduledDishChangeService = scheduledDishChangeService;
        this.checkoutService = checkoutService;
    }

    private UUID getOwnerIdFromToken(@AuthenticationPrincipal Jwt token) {
        return UUID.fromString(token.getClaimAsString("sub"));
    }


    @PreAuthorize("hasAuthority('owner')")
    @PostMapping("/addRestaurant")
    public ResponseEntity<?> addRestaurant(@RequestBody RestaurantDto restaurantDto, @AuthenticationPrincipal Jwt token) {
        try {

            UUID ownerId = getOwnerIdFromToken(token);

            CreateRestaurantCommand command = new CreateRestaurantCommand(
                    ownerId,
                    restaurantDto.addressId(),
                    restaurantDto.restaurantType(),
                    restaurantDto.name(),
                    restaurantDto.email(),
                    restaurantDto.logo(),
                    restaurantDto.dishes()
            );

            Restaurant restaurant = restaurantService.createRestaurant(command);
            return ResponseEntity.ok(RestaurantDto.from(restaurant));

        } catch (IllegalStateException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
    @PreAuthorize("hasAuthority('owner')")
    @PostMapping("/addDish")
    public ResponseEntity<RestaurantDto.DishDto> addDish(@RequestBody DishDto dishDto) {
        CreateDishCommand command = new CreateDishCommand(
                dishDto.RestaurantId(),
                dishDto.name(),
                dishDto.description(),
                dishDto.price(),
                DishState.NOT_PUBLISHED,
                dishDto.preparationTime()
        );
        Dish dish = restaurantService.createDish(command);
        return ResponseEntity.ok(DishDto.from(dish,dishDto.RestaurantId()));
    }

    @GetMapping("/dish/{id}")
    public ResponseEntity<RestaurantDto.DishDto> getDish(@PathVariable("id") UUID id) {
        Restaurant restaurant = restaurantService.GetRestaurantWothDishFromDish(id);
        Dish dish = restaurant.getDishes().stream().filter(d -> d.getId().id().equals(id)).findFirst().orElseThrow();
        return ResponseEntity.ok(DishDto.from(dish,restaurant.getId().id()));
    }

    @PreAuthorize("hasAuthority('owner')")
    @GetMapping("/{restaurantId}/dishes")
    public ResponseEntity<RestaurantDto> addDish(@PathVariable("restaurantId") UUID restaurantId) {

        Restaurant restaurant = restaurantService.getRestaurantById(restaurantId);

        return ResponseEntity.ok(RestaurantDto.from(restaurant));
    }
    @PreAuthorize("hasAuthority('owner')")
    @PutMapping("/changeStateDish/{id}")
    public ResponseEntity<Void> changeStateDish(@PathVariable("id") UUID id,
                                                         @RequestBody DishState state) {
        restaurantService.updateStateDish(id,state);
        return ResponseEntity.ok().build();
    }
    @PreAuthorize("hasAuthority('owner')")
    @PutMapping("/{restaurantId}/changeOpenState")
    public ResponseEntity<Void> changeOpenState(
            @PathVariable UUID restaurantId,
            @AuthenticationPrincipal Jwt token
    ) {
        UUID ownerId = getOwnerIdFromToken(token);
        restaurantService.updateOpenState(restaurantId, ownerId);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAuthority('owner')")
    @PostMapping("/scheduleDishChange")
    public ResponseEntity<Void> scheduleDishChange(@RequestBody ScheduleDishChangeDto request) {
        scheduledDishChangeService.scheduleDishChange(request);
        return ResponseEntity.ok().build();
    }
    @PreAuthorize("hasAuthority('owner')")
    @PostMapping("/applyAllScheduledChangesForRestaurant")
    public ResponseEntity<Void> applyAllScheduledChanges(
            @RequestParam UUID restaurantId,
            @AuthenticationPrincipal Jwt token
    ) {
        UUID ownerId = getOwnerIdFromToken(token);
        scheduledDishChangeService.applyAllPendingChanges(ownerId, restaurantId);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAuthority('owner')")
    @GetMapping("/{restaurantId}/changesOverview")
    public ResponseEntity<RestaurantChangesOverviewDto> getRestaurantChangesOverview(
            @PathVariable UUID restaurantId,
            @AuthenticationPrincipal Jwt token
    ) {
        UUID ownerId = getOwnerIdFromToken(token);
        var overview = restaurantService.getOverviewForRestaurantAndOwner(restaurantId, ownerId);
        return ResponseEntity.ok(overview);
    }
    @PreAuthorize("hasAuthority('owner')")
    @PostMapping("/{restaurantId}/openinghour")
    public ResponseEntity<RestaurantDto.OpeningHourDto> addOpeningsHour(
            @PathVariable UUID restaurantId,
            @RequestBody RestaurantDto.OpeningHourDto openingHourDto
            ) {

        var overview = restaurantService.addOpenhours(restaurantId, openingHourDto.closingTime(),openingHourDto.openingTime(),openingHourDto.dayOfWeek());
        return ResponseEntity.ok(RestaurantDto.OpeningHourDto.from(overview));
    }

    @GetMapping("/get")
    public ResponseEntity<List<GetAllRestaurantDto>> getOpeningHours(){
        List<Restaurant> restaurants = restaurantService.getAllRestaurants();
        return ResponseEntity.ok(restaurants.stream().map(GetAllRestaurantDto::from).toList());
    }



    @PostMapping("/prepareCheckout")
    public ResponseEntity<?> prepareCheckout(@RequestBody CheckoutRequestDto checkoutRequest) {
        try {
            var response = checkoutService.prepareCheckout(checkoutRequest);
            return ResponseEntity.ok(response);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }
    @PostMapping("/checkout")
    public ResponseEntity<?> checkout(@RequestBody CheckoutRequestDto checkoutRequest) {
        try {
            var response = checkoutService.checkout(checkoutRequest);
            return ResponseEntity.ok(response);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

}
