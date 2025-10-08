package be.kdg.sa.restaurantservice.application;

import be.kdg.sa.restaurantservice.api.CheckoutRequestDto;
import be.kdg.sa.restaurantservice.api.CheckoutResponseDto;
import be.kdg.sa.restaurantservice.domain.NotFoundException;
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
         //  throw new NotFoundException("Restaurant is gesloten of kan bestelling niet op tijd klaarmaken");
        }


        return new CheckoutResponseDto(checkoutRequest.orderId(),true);
    }

    // deze methode moet wrs ook nog aangepast worden
    public CheckoutResponseDto checkout(CheckoutRequestDto checkoutRequest) {
        // Herberekenen
        CheckoutResponseDto prepared = prepareCheckout(checkoutRequest);

        if (!prepared.canBePrepared()) {
            throw new NotFoundException("Restaurant is gesloten of kan bestelling niet op tijd klaarmaken");
        }


        return prepared;
    }

}