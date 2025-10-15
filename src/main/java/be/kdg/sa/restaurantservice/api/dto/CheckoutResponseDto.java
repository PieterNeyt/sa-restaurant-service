package be.kdg.sa.restaurantservice.api.dto;

import java.util.UUID;


public record CheckoutResponseDto(
        UUID orderId,
        boolean success,
        String message
) {
}