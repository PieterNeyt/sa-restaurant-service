package be.kdg.sa.restaurantservice.domain.order;

import be.kdg.sa.restaurantservice.api.dto.CheckoutRequestDto;
import be.kdg.sa.restaurantservice.application.command.CheckOutRequestCommand;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderLine(UUID dishId,
                        String name,
                        BigDecimal price,
                        int quantity,
                        int preparationTime
) {
    public static OrderLine from(CheckOutRequestCommand.OrderLineCommand orderline) {
        return new OrderLine(orderline.dishId(),
                orderline.name(),
                orderline.price(),
                orderline.quantity(),
                orderline.preparationTime());
    }
}
