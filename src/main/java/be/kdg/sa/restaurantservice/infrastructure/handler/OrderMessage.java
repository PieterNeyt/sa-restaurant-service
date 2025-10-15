package be.kdg.sa.restaurantservice.infrastructure.handler;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderMessage(UUID id,
                           UUID restaurantId,
                           BigDecimal totalPrice) {
}

