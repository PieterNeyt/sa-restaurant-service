package be.kdg.sa.restaurantservice.application.command;

import java.util.UUID;

public record RestaurantResponse(UUID orderId,
                                 String isAccepted,
                                 String message) {
}
