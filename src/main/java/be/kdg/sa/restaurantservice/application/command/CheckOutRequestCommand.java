package be.kdg.sa.restaurantservice.application.command;

import be.kdg.sa.restaurantservice.api.dto.CheckoutRequestDto;
import be.kdg.sa.restaurantservice.application.command.CheckOutRequestCommand;
import org.hibernate.annotations.Check;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record CheckOutRequestCommand(UUID orderId,
                                     UUID restaurantId,
                                     UUID clientId,
                                     List<CheckOutRequestCommand.OrderLineCommand> items
) {
    public static CheckOutRequestCommand from(CheckoutRequestDto checkoutRequest) {
        return new CheckOutRequestCommand(checkoutRequest.orderId(),
                checkoutRequest.restaurantId(),
                checkoutRequest.clientId(),
                checkoutRequest.items().stream().map(OrderLineCommand::from).toList());
    }

    public record OrderLineCommand(
            UUID dishId,
            String name,
            BigDecimal price,
            int quantity,
            int preparationTime
    ) {
        public static OrderLineCommand from(CheckoutRequestDto.OrderLineDto orderline) {
            return new OrderLineCommand(orderline.dishId(),
                    orderline.name(),
                    orderline.price(),
                    orderline.quantity(),
                    orderline.preparationTime());
        }
    }
}
