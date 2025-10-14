package be.kdg.sa.restaurantservice.api;

import be.kdg.sa.restaurantservice.application.CheckoutService;
import be.kdg.sa.restaurantservice.application.command.CreateRestaurantCommand;
import be.kdg.sa.restaurantservice.application.RestaurantService;
import be.kdg.sa.restaurantservice.application.ScheduledRestaurantChangeService;


import be.kdg.sa.restaurantservice.domain.address.AddressId;
import be.kdg.sa.restaurantservice.domain.owner.OwnerId;
import be.kdg.sa.restaurantservice.domain.restaurant.PriceCategory;
import be.kdg.sa.restaurantservice.domain.restaurant.Restaurant;
import be.kdg.sa.restaurantservice.domain.restaurant.RestaurantId;
import be.kdg.sa.restaurantservice.domain.restaurant.RestaurantType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;


import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RestaurantController.class)
@AutoConfigureMockMvc
class RestaurantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RestaurantService restaurantService;

    @MockitoBean
    private ScheduledRestaurantChangeService scheduledDishChangeService;

    @MockitoBean
    private CheckoutService checkoutService;  // Mock ook deze dependency

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void addRestaurant_returnsOkAndRestaurantDto() throws Exception {
        // Arrange: test data
        UUID ownerId = UUID.randomUUID();
        UUID addressId = UUID.randomUUID();
        UUID restaurantId = UUID.randomUUID();

        RestaurantDto restaurantDto = new RestaurantDto(
                null, // ID is null bij create
                ownerId,
                addressId,
                RestaurantType.PIZZERIA,
                "Dominos",
                "test@example.com",
                "logo.png",
                List.of(),
                true,
                PriceCategory.NORMAL,
                List.of()
        );

        // Mock restaurant dat teruggegeven wordt door de service
        Restaurant mockRestaurant = new Restaurant(
                new OwnerId(ownerId),
                new AddressId(addressId),
                RestaurantType.PIZZERIA,
                "Dominos",
                "test@example.com",
                "logo.png"
        );
        // Stel ID in via reflectie of gebruik een test constructor indien beschikbaar
        ReflectionTestUtils.setField(mockRestaurant, "id", new RestaurantId(restaurantId));

        // Configureer mock gedrag
        when(restaurantService.createRestaurant(any(CreateRestaurantCommand.class)))
                .thenReturn(mockRestaurant);

        // Act & Assert
        mockMvc.perform(post("/api/restaurant/addRestaurant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(restaurantDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(restaurantId.toString()))
                .andExpect(jsonPath("$.ownerId").value(ownerId.toString()))
                .andExpect(jsonPath("$.addressId").value(addressId.toString()))
                .andExpect(jsonPath("$.restaurantType").value("PIZZERIA"))
                .andExpect(jsonPath("$.name").value("Dominos"))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.logo").value("logo.png"));

        // Verify dat de service aangeroepen werd
        verify(restaurantService).createRestaurant(argThat(command ->
                command.ownerId().equals(ownerId) &&
                        command.addressId().equals(addressId) &&
                        command.restaurantType() == RestaurantType.PIZZERIA &&
                        command.name().equals("Dominos") &&
                        command.email().equals("test@example.com") &&
                        command.logo().equals("logo.png")
        ));
    }

    @Test
    void addRestaurant_whenOwnerAlreadyOwnsRestaurant_returnsBadRequest() throws Exception {
        // Arrange
        UUID ownerId = UUID.randomUUID();
        UUID addressId = UUID.randomUUID();

        RestaurantDto restaurantDto = new RestaurantDto(
                null,
                ownerId,
                addressId,
                RestaurantType.PIZZERIA,
                "Dominos",
                "test@example.com",
                "logo.png",
                List.of(),
                true,
                PriceCategory.NORMAL,
                List.of()
        );

        // Mock dat owner al een restaurant heeft
        when(restaurantService.createRestaurant(any(CreateRestaurantCommand.class)))
                .thenThrow(new IllegalStateException("Owner already owns restaurant"));

        // Act & Assert
        mockMvc.perform(post("/api/restaurant/addRestaurant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(restaurantDto)))
                .andExpect(status().is4xxClientError());
    }
}