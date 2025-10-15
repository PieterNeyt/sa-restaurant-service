package be.kdg.sa.restaurantservice.application.command;

import java.util.UUID;

public record RestaurantResponse(UUID orderId,
                                 boolean isAccepted,
                                 String message) {
}
