package be.kdg.sa.restaurantservice.application.command;

import java.util.UUID;

public record CheckOutResponseCommand(UUID orderId,
                                      boolean success,
                                      String message) {
}
