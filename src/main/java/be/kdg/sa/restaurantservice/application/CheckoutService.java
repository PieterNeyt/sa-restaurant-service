package be.kdg.sa.restaurantservice.application;

import be.kdg.sa.restaurantservice.api.CheckoutRequestDto;
import be.kdg.sa.restaurantservice.api.CheckoutResponseDto;
import be.kdg.sa.restaurantservice.domain.NotFoundException;
import be.kdg.sa.restaurantservice.domain.restaurant.DishState;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Service
@Transactional
public class CheckoutService {

    private final RestaurantService restaurantService;

    public CheckoutService(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    public CheckoutResponseDto prepareCheckout(CheckoutRequestDto checkoutRequest) {
        var restaurant = restaurantService.getRestaurantById(checkoutRequest.restaurantId());

        if (restaurant == null) {
            throw new NotFoundException("Restaurant niet gevonden");
        }

        DayOfWeek today = LocalDate.now().getDayOfWeek();
        var openingHoursToday = restaurant.getOpeningHours().stream()
                .filter(oh -> oh.getDayOfWeek() == today)
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Geen openingstijden beschikbaar voor vandaag"));

        var now = LocalTime.now();

        int maxPreparationMinutes = checkoutRequest.items().stream()
                .mapToInt(CheckoutRequestDto.OrderLineDto::preparationTime)
                .max()
                .orElse(0);

        var expectedFinishTime = now.plusMinutes(maxPreparationMinutes);

        if (!openingHoursToday.isOpenAt(now) || expectedFinishTime.isAfter(openingHoursToday.getClosingTime())) {
          throw new NotFoundException("Restaurant is gesloten of kan bestelling niet op tijd klaarmaken");
        }


        return new CheckoutResponseDto(checkoutRequest.orderId(), true, "Checkout voorbereid");
    }


    public CheckoutResponseDto checkout(CheckoutRequestDto checkoutRequest) {

        prepareCheckout(checkoutRequest);


        for (var item : checkoutRequest.items()) {
            var restaurant = restaurantService.GetRestaurantWothDishFromDish(item.dishId());
            var dish = restaurant.getDishes().stream()
                    .filter(d -> d.getId().id().equals(item.dishId()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Dish not found"));


            if (dish.getPrice().compareTo(item.price()) != 0 ||
                    !dish.getName().equals(item.name()) ||
                    dish.getPreparationTime() != item.preparationTime()) {

                throw new IllegalStateException(String.format(
                        "Dish %s is gewijzigd (prijs of eigenschappen verschillen). " +
                                "Verwacht: €%s, actueel: €%s",
                        dish.getName(), item.price(), dish.getPrice()
                ));
            }

            if (dish.getState() != DishState.PUBLISHED) {
                throw new IllegalStateException("Dish " + dish.getName() + " is niet beschikbaar.");
            }
        }

        return new CheckoutResponseDto(checkoutRequest.orderId(), true, "Checkout succesvol");
    }


}