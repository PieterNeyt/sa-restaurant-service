package be.kdg.sa.restaurantservice.api;

import be.kdg.sa.restaurantservice.api.dto.*;
import be.kdg.sa.restaurantservice.api.dto.RestaurantDto.DishDto;
import be.kdg.sa.restaurantservice.api.dto.RestaurantDto.RestaurantChangesOverviewDto;
import be.kdg.sa.restaurantservice.api.dto.RestaurantDto.ScheduleDishChangeDto;
import be.kdg.sa.restaurantservice.application.*;
import be.kdg.sa.restaurantservice.application.command.CheckOutRequestCommand;
import be.kdg.sa.restaurantservice.application.command.CreateDishCommand;
import be.kdg.sa.restaurantservice.application.command.CreateRestaurantCommand;
import be.kdg.sa.restaurantservice.domain.order.Order;
import be.kdg.sa.restaurantservice.domain.restaurant.dish.Dish;
import be.kdg.sa.restaurantservice.domain.restaurant.dish.DishState;
import be.kdg.sa.restaurantservice.domain.restaurant.Restaurant;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/restaurant")
public class RestaurantController {
    private final RestaurantService restaurantService;
    private final ScheduledRestaurantChangeService scheduledDishChangeService;
    private final CheckoutService checkoutService;
    private final OrderService orderService;

    public RestaurantController(RestaurantService restaurantService, ScheduledRestaurantChangeService scheduledDishChangeService, CheckoutService checkoutService, OrderService orderService) {
        this.restaurantService = restaurantService;
        this.scheduledDishChangeService = scheduledDishChangeService;
        this.checkoutService = checkoutService;
        this.orderService = orderService;
    }

    private UUID getOwnerIdFromToken(@AuthenticationPrincipal Jwt token) {
        return UUID.fromString(token.getClaimAsString("sub"));
    }


    @GetMapping("/{restaurantId}")
    public ResponseEntity<RestaurantDto> getAllDishes(@PathVariable("restaurantId") UUID restaurantId) {
        Restaurant restaurant = restaurantService.getRestaurantById(restaurantId);

        return ResponseEntity.ok(RestaurantDto.from(restaurant));
    }

    @PreAuthorize("hasAuthority('owner')")
    @PostMapping("/restaurant")
    public ResponseEntity<?> addRestaurant(@RequestBody RestaurantDto restaurantDto, @AuthenticationPrincipal Jwt token) {

        UUID ownerId = getOwnerIdFromToken(token);
        CreateRestaurantCommand command = new CreateRestaurantCommand(
                ownerId,
                restaurantDto.restaurantType(),
                restaurantDto.name(),
                restaurantDto.email(),
                restaurantDto.logo(),
                restaurantDto.dishes(),
                restaurantDto.address().city(),
                restaurantDto.address().streetNumber(),
                restaurantDto.address().street(),
                restaurantDto.address().postalCode(),
                restaurantDto.address().country()
        );

        Restaurant restaurant = restaurantService.createRestaurant(command);
        return ResponseEntity.ok(RestaurantDto.from(restaurant));
    }

    @GetMapping()
    public ResponseEntity<List<GetAllRestaurantDto>> getRestaurants() {
        List<Restaurant> restaurants = restaurantService.getAllRestaurants();
        return ResponseEntity.ok(restaurants.stream().map(GetAllRestaurantDto::from).toList());
    }


    @PreAuthorize("hasAuthority('owner')")
    @PostMapping("/dish")
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
        return ResponseEntity.ok(DishDto.from(dish, dishDto.RestaurantId()));
    }

    @GetMapping("/dish/{id}")
    public ResponseEntity<RestaurantDto.DishDto> getDish(@PathVariable("id") UUID id) {
        Dish dish = restaurantService.getDishFromDishId(id);
        return ResponseEntity.ok(DishDto.from(dish, null));
    }

    @PreAuthorize("hasAuthority('owner')")
    @PutMapping("/dish/{id}/state")
    public ResponseEntity<RestaurantDto> addDish(@PathVariable("id") UUID restaurantId) {

        Restaurant restaurant = restaurantService.getRestaurantById(restaurantId);
        return ResponseEntity.ok(RestaurantDto.from(restaurant));
    }

    @PreAuthorize("hasAuthority('owner')")
    @PutMapping("/changeStateDish/{id}")
    public ResponseEntity<Void> changeStateDish(@PathVariable("id") UUID id,
                                                @RequestBody DishState state) {
        restaurantService.updateStateDish(id, state);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAuthority('owner')")
    @PutMapping("/{id}/changeOpenState")
    public ResponseEntity<Void> changeOpenState(
            @PathVariable("id") UUID restaurantId,
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
        return ResponseEntity.ok(RestaurantDto.RestaurantChangesOverviewDto.from(overview));
    }

    @PreAuthorize("hasAuthority('owner')")
    @PostMapping("/{restaurantId}/openinghour")
    public ResponseEntity<RestaurantDto.OpeningHourDto> addOpeningsHour(
            @PathVariable UUID restaurantId,
            @RequestBody RestaurantDto.OpeningHourDto openingHourDto
    ) {

        var overview = restaurantService.addOpenhours(restaurantId, openingHourDto.closingTime(), openingHourDto.openingTime(), openingHourDto.dayOfWeek());
        return ResponseEntity.ok(RestaurantDto.OpeningHourDto.from(overview));
    }


    @PostMapping("/prepareCheckout")
    public ResponseEntity<CheckoutResponseDto> prepareCheckout(@RequestBody CheckoutRequestDto checkoutRequest) {
        var checkOutRequestCommand = CheckOutRequestCommand.from(checkoutRequest);
        var response = checkoutService.checkout(checkOutRequestCommand);

        return ResponseEntity.ok(CheckoutResponseDto.from(response));

    }

    @PostMapping("/checkout")
    public ResponseEntity<CheckoutResponseDto> checkout(@RequestBody CheckoutRequestDto checkoutRequest) {
        var checkOutRequestCommand = CheckOutRequestCommand.from(checkoutRequest);
        var response = checkoutService.checkout(checkOutRequestCommand);
        return ResponseEntity.ok(CheckoutResponseDto.from(response));
    }

    @PreAuthorize("hasAuthority('owner')")
    @GetMapping("/{id}/orders")
    public ResponseEntity<List<OrderDto>> getAllOrderFromRestaurant(@PathVariable("id") UUID restaurantId) {
        List<Order> orders = orderService.getAllOrderFromRestaurant(restaurantId);
        return ResponseEntity.ok(orders.stream().map(OrderDto::fromDomain).toList());
    }

    @PreAuthorize("hasAuthority('owner')")
    @PutMapping("/{restaurantId}/order/{orderId}/accept")
    public ResponseEntity<Void> acceptOrder(@PathVariable("orderId") UUID orderId,
                                            @PathVariable("restaurantId") UUID restaurantId
                                            ) {
        orderService.acceptOrder(orderId,restaurantId);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAuthority('owner')")
    @PutMapping("/{restaurantId}/order/{orderId}/deny")
    public ResponseEntity<Void> denyOrder(@PathVariable("orderId") UUID orderId,
                                          @PathVariable("restaurantId") UUID restaurantId,
                                          @RequestBody String message) {
        orderService.denyOrder(orderId,restaurantId,message);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAuthority('owner')")
    @PutMapping("/{restaurantId}/order/{orderId}/ready")
    public ResponseEntity<Void> orderIsReady(@PathVariable("orderId") UUID orderId,
                                          @PathVariable("restaurantId") UUID restaurantId) {
        orderService.orderIsReady(orderId,restaurantId);
        return ResponseEntity.ok().build();
    }

}
