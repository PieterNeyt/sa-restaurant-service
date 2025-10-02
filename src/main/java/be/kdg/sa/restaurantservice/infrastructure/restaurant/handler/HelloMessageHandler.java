package be.kdg.sa.restaurantservice.infrastructure.restaurant.handler;


import be.kdg.sa.restaurantservice.infrastructure.restaurant.config.RabbitMQTopology;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class HelloMessageHandler {

    private static final Logger log = LoggerFactory.getLogger(HelloMessageHandler.class);

    @RabbitListener(queues = RabbitMQTopology.HELLO_QUEUE_NAME)
    void onHelloMessageReceived(HelloMessage message) {
        log.info("hello: {}", message);
    }

    @RabbitListener(queues = RabbitMQTopology.SOMETHING_QUEUE_NAME)
    void onSomethingMessageReceived(HelloMessage message) {
        log.info("something: {}", message);
    }
}
