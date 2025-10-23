package be.kdg.sa.restaurantservice.domain.order;


import be.kdg.sa.restaurantservice.application.command.RestaurantResponse;

public interface IOrderMessageService {
    void sendReadyResponse(RestaurantResponse restaurantResponse);

    void sendAcceptResponse(RestaurantResponse restaurantResponse);

    void sendDenyResponse(RestaurantResponse restaurantResponse);
}
