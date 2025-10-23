package be.kdg.sa.restaurantservice.domain.order;


import be.kdg.sa.restaurantservice.application.command.RestaurantResponse;

public interface IOrderMessagePublisher {
    void sendReadyResponse(RestaurantResponse restaurantResponse);

    void sendAcceptResponse(RestaurantResponse restaurantResponse);

    void sendDenyResponse(RestaurantResponse restaurantResponse);
}
