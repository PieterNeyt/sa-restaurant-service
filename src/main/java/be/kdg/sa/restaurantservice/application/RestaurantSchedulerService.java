package be.kdg.sa.restaurantservice.application;

import be.kdg.sa.restaurantservice.application.command.RestaurantResponse;
import be.kdg.sa.restaurantservice.domain.order.Order;
import be.kdg.sa.restaurantservice.domain.order.OrderRepository;
import be.kdg.sa.restaurantservice.domain.restaurant.Restaurant;
import be.kdg.sa.restaurantservice.domain.restaurant.RestaurantRepository;
import be.kdg.sa.restaurantservice.domain.schedulechange.ScheduledDishChange;
import be.kdg.sa.restaurantservice.domain.schedulechange.ScheduledDishChangeRepository;
import be.kdg.sa.restaurantservice.infrastructure.config.RabbitMQTopology;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class RestaurantSchedulerService {


    private final ScheduledDishChangeRepository scheduledRepo;
    private final RestaurantRepository restRepo;
    private final OrderRepository orderRepo;
    private final RabbitTemplate rabbitTemplate;

    @Scheduled(fixedRate = 60000) // elke minuut
    public void executeScheduledDishChanges() {
        List<ScheduledDishChange> dueChanges = scheduledRepo.findDueChanges(LocalDateTime.now());

        for (ScheduledDishChange change : dueChanges) {
            Restaurant restaurant = restRepo.findRestaurantFromDishId(change.getDishId().id())
                    .orElseThrow(() -> new RuntimeException("Restaurant not found"));
            restaurant.updateDish(
                    change.getDishId(),
                    change.getTargetState(),
                    change.getTargetName(),
                    change.getTargetPrice(),
                    change.getTargetDescription(),
                    change.getPreparationTime());

            restRepo.save(restaurant);
            scheduledRepo.save(change);
        }
    }

    @Scheduled(fixedRate = 60000) // elke minuut
    public void executeScheduledOrderChanges() {
        List<Order> orders = orderRepo.findAllPendingOrders();

        for (Order order : orders) {

            if(order.has5minutsPassed()){
                order.deny("Restaurant heeft niet binnen de 5 minute geantwoord");

                rabbitTemplate.convertAndSend( RabbitMQTopology.RESTAURANT_RESPONSE_EXCHANGE_NAME,
                        "order.response." + order.getOrderId().id(),
                        new RestaurantResponse(order.getOrderId().id(),order.getStatus().toString(), order.getMessage()));

                orderRepo.delete(order);
            }
        }
    }

}