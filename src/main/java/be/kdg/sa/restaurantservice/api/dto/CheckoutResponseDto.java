package be.kdg.sa.restaurantservice.api.dto;

import be.kdg.sa.restaurantservice.application.command.CheckOutResponseCommand;

import java.util.UUID;


public record CheckoutResponseDto(
        UUID orderId,
        boolean success,
        String message
) {
    public static CheckoutResponseDto from(CheckOutResponseCommand response) {
        return new CheckoutResponseDto(response.orderId(),response.success(),response.message());
    }
}