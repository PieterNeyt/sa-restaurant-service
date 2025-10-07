package be.kdg.sa.restaurantservice.api;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record CheckoutRequestDto(
        UUID orderId,
        UUID restaurantId,
        UUID clientId,
        List<OrderLineDto> items
) {
    public record OrderLineDto(
            UUID dishId,
            String name,
            BigDecimal price,
            int quantity,
            int preparationTime
    ) {}
}