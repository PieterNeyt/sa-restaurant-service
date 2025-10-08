package be.kdg.sa.restaurantservice.api;

import java.math.BigDecimal;
import java.util.UUID;


public record CheckoutResponseDto(
        UUID orderId,
        boolean canBePrepared // moet wrs nog aangepast worden
) {
}