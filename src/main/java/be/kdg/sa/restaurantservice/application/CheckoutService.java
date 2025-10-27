package be.kdg.sa.restaurantservice.application;

import be.kdg.sa.restaurantservice.application.command.CheckOutRequestCommand;
import be.kdg.sa.restaurantservice.application.command.CheckOutResponseCommand;
import be.kdg.sa.restaurantservice.domain.order.OrderLine;
import be.kdg.sa.restaurantservice.domain.restaurant.Restaurant;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CheckoutService {

    private final RestaurantService restaurantService;

    public CheckoutService(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    public void prepareCheckout(CheckOutRequestCommand checkoutRequest) {
        Restaurant restaurant = restaurantService.getRestaurantById(checkoutRequest.restaurantId());
        List<OrderLine> items = checkoutRequest.items()
                .stream()
                .map(OrderLine::from)
                .toList();

        restaurant.prepareCheckout(items);
        new CheckOutResponseCommand(checkoutRequest.orderId(), true, "Checkout prepared");
    }


    public CheckOutResponseCommand checkout(CheckOutRequestCommand checkoutRequest) {
        prepareCheckout(checkoutRequest);

        checkoutRequest.items()
                .forEach(item -> {
            var restaurant = restaurantService.GetRestaurantWithDishFromDish(item.dishId());
            restaurant.checkDish(OrderLine.from(item));
        });

        return new CheckOutResponseCommand(checkoutRequest.orderId(), true, "Checkout succesvol");
    }


}