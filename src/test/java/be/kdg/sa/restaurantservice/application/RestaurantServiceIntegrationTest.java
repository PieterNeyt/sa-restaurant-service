package be.kdg.sa.restaurantservice.application;

import be.kdg.sa.restaurantservice.TestHelper;
import be.kdg.sa.restaurantservice.api.dto.CheckoutRequestDto;
import be.kdg.sa.restaurantservice.api.dto.CheckoutResponseDto;
import be.kdg.sa.restaurantservice.application.command.CheckOutRequestCommand;
import be.kdg.sa.restaurantservice.application.command.CheckOutResponseCommand;
import be.kdg.sa.restaurantservice.application.command.CreateDishCommand;
import be.kdg.sa.restaurantservice.application.command.CreateRestaurantCommand;
import be.kdg.sa.restaurantservice.domain.restaurant.*;
import be.kdg.sa.restaurantservice.domain.restaurant.dish.Dish;
import be.kdg.sa.restaurantservice.domain.restaurant.dish.DishState;
import be.kdg.sa.restaurantservice.domain.schedulechange.ScheduledDishChange;
import be.kdg.sa.restaurantservice.domain.schedulechange.ScheduledDishChangeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class RestaurantServiceIntegrationTest {

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private ScheduledDishChangeRepository scheduledDishChangeRepository;

    @Autowired
    private RestaurantSchedulerService restaurantSchedulerService;

    @Autowired
    private ScheduledRestaurantChangeService scheduledRestaurantChangeService;
    @Autowired
    private CheckoutService checkoutService;

    @Autowired
    private TestHelper testHelper;

    @AfterEach
    void cleanup() {
        testHelper.cleanUp();
        scheduledDishChangeRepository.deleteAll();
    }

    @Test
    void createRestaurant_shouldSaveRestaurantWithAllDetails() {
        // Arrange
        UUID ownerId = UUID.randomUUID();

        CreateRestaurantCommand command = new CreateRestaurantCommand(
                ownerId,
                RestaurantType.PIZZERIA,
                "Pizza Palace",
                "info@pizzapalace.com",
                "palace-logo.png",
                List.of(),
                "Antwerpen",
                "1",
                "Groenplaats",
                "2000",
                "Belgium"
        );

        // Act
        Restaurant createdRestaurant = restaurantService.createRestaurant(command);

        // Assert
        assertThat(createdRestaurant).isNotNull();
        assertThat(createdRestaurant.getId()).isNotNull();
        assertThat(createdRestaurant.getName()).isEqualTo("Pizza Palace");
        assertThat(createdRestaurant.getEmail()).isEqualTo("info@pizzapalace.com");

        // Verify persistence
        Restaurant retrievedRestaurant = restaurantRepository.findById(createdRestaurant.getId().id())
                .orElseThrow();
        assertThat(retrievedRestaurant.getName()).isEqualTo("Pizza Palace");
        assertThat(retrievedRestaurant.getRestaurantType()).isEqualTo(RestaurantType.PIZZERIA);
    }

    @Test
    void createRestaurant_whenOwnerAlreadyOwnsRestaurant_shouldThrowException() {
        // Arrange
        UUID ownerId = UUID.randomUUID();

        CreateRestaurantCommand command = new CreateRestaurantCommand(
                ownerId,
                RestaurantType.BUFFET,
                "First Restaurant",
                "first@test.com",
                "logo1.png",
                List.of(),
                "Antwerpen",
                "1",
                "Groenplaats",
                "2000",
                "Belgium"
        );

        // Create first restaurant
        restaurantService.createRestaurant(command);

        // Create second command with same owner
        CreateRestaurantCommand duplicateCommand = new CreateRestaurantCommand(
                ownerId,
                RestaurantType.SEAFOOD,
                "Second Restaurant",
                "second@test.com",
                "logo2.png",
                List.of(),
                "Antwerpen",
                "1",
                "Groenplaats",
                "2000",
                "Belgium"
        );

        // Act & Assert
        assertThatThrownBy(() -> restaurantService.createRestaurant(duplicateCommand))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Owner already owns restaurant");
    }

    @Test
    void createDishAndUpdateState_shouldSaveDishAndReflectStateChange(){
        // Arrange
        Restaurant restaurant = testHelper.saveRestaurent();
        CreateDishCommand dishCommand = new CreateDishCommand(
                restaurant.getId().id(),
                "Margherita Pizza",
                "Classic Italian pizza",
                new BigDecimal("12.50"),
                DishState.NOT_PUBLISHED,
                30
        );

        // Act
        Dish createdDish = restaurantService.createDish(dishCommand);

        // Assert
        assertThat(createdDish).isNotNull();
        assertThat(createdDish.getName()).isEqualTo("Margherita Pizza");
        assertThat(createdDish.getState()).isEqualTo(DishState.NOT_PUBLISHED);

        // Act
        restaurantService.updateStateDish(createdDish.getId().id(), DishState.PUBLISHED);

        // Assert
        Restaurant updatedRestaurant = restaurantRepository.findById(restaurant.getId().id())
                .orElseThrow();
        Dish updatedDish = updatedRestaurant.getDishes().stream()
                .filter(d -> d.getId().id().equals(createdDish.getId().id()))
                .findFirst()
                .orElseThrow();
        assertThat(updatedDish.getState()).isEqualTo(DishState.PUBLISHED);
    }

    @Test
    void scheduledDishChange_shouldBeAppliedAutomaticallyByScheduler() {
        // Arrange
        Restaurant restaurant = testHelper.saveRestaurent();
        CreateDishCommand dishCommand = new CreateDishCommand(
                restaurant.getId().id(),
                "Original Pizza",
                "Original description",
                new BigDecimal("10.00"),
                DishState.PUBLISHED,
                25
        );
        Dish dish = restaurantService.createDish(dishCommand);

        // Create scheduled change
        ScheduledDishChange scheduledChange = new ScheduledDishChange(
                dish.getId(),
                LocalDateTime.now().minusMinutes(5),
                DishState.NOT_PUBLISHED,
                "Updated Pizza Name",
                "Updated description",
                new BigDecimal("15.00"),
                35
        );
        scheduledDishChangeRepository.save(scheduledChange);

        // Act
        restaurantSchedulerService.executeScheduledDishChanges();

        // Assert
        Restaurant updatedRestaurant = restaurantRepository.findRestaurantFromDishId(dish.getId().id())
                .orElseThrow();
        Dish updatedDish = updatedRestaurant.getDishes().stream()
                .filter(d -> d.getId().id().equals(dish.getId().id()))
                .findFirst()
                .orElseThrow();

        assertThat(updatedDish.getName()).isEqualTo("Updated Pizza Name");
        assertThat(updatedDish.getDescription()).isEqualTo("Updated description");
        assertThat(updatedDish.getPrice()).isEqualByComparingTo(new BigDecimal("15.00"));
        assertThat(updatedDish.getState()).isEqualTo(DishState.NOT_PUBLISHED);
        assertThat(updatedDish.getPreparationTime()).isEqualTo(35);
    }

    @Test
    void addOpeningHoursAndToggleRestaurantState_shouldSaveOpeningHoursAndAllowStateToggling() {
        // Arrange
        UUID ownerId = UUID.randomUUID();

        CreateRestaurantCommand command = new CreateRestaurantCommand(
                ownerId,
                RestaurantType.PIZZERIA,
                "Trattoria Roma",
                "roma@test.com",
                "roma-logo.png",
                List.of(),
                "Antwerpen",
                "1",
                "Groenplaats",
                "2000",
                "Belgium"
        );
        Restaurant restaurant = restaurantService.createRestaurant(command);

        // Act
         restaurantService.addOpenhours(
                restaurant.getId().id(),
                LocalTime.of(22, 0),
                LocalTime.of(11, 0),
                DayOfWeek.MONDAY
        );

         restaurantService.addOpenhours(
                restaurant.getId().id(),
                LocalTime.of(23, 30),
                LocalTime.of(11, 0),
                DayOfWeek.FRIDAY
        );

        // Assert
        Restaurant updatedRestaurant = restaurantRepository.findById(restaurant.getId().id())
                .orElseThrow();
        assertThat(updatedRestaurant.getOpeningHours()).hasSize(2);
        assertThat(updatedRestaurant.getOpeningHours())
                .anyMatch(oh -> oh.getDayOfWeek() == DayOfWeek.MONDAY
                        && oh.getOpeningTime().equals(LocalTime.of(11, 0))
                        && oh.getClosingTime().equals(LocalTime.of(22, 0)));

        // Act - Toggle open state
        boolean initialState = updatedRestaurant.isOpen();
        restaurantService.updateOpenState(restaurant.getId().id(), ownerId);

        // Assert - Verify state toggle
        Restaurant toggledRestaurant = restaurantRepository.findById(restaurant.getId().id())
                .orElseThrow();
        assertThat(toggledRestaurant.isOpen()).isNotEqualTo(initialState);

        // Toggle again
        restaurantService.updateOpenState(restaurant.getId().id(), ownerId);
        Restaurant reToggledRestaurant = restaurantRepository.findById(restaurant.getId().id())
                .orElseThrow();
        assertThat(reToggledRestaurant.isOpen()).isEqualTo(initialState);
    }
    @Test
    void getOverviewForRestaurantAndOwner_shouldReturnOverviewWithPendingChanges() {
        // Arrange
        Restaurant restaurant = testHelper.saveRestaurent();
        UUID ownerId = restaurant.getOwnerId().id();

        // Maak een gerecht aan
        var dishCommand = new CreateDishCommand(
                restaurant.getId().id(),
                "Test Pizza",
                "Lekker testgerecht",
                new BigDecimal("9.99"),
                DishState.NOT_PUBLISHED,
                20
        );
        var dish = restaurantService.createDish(dishCommand);

        // Voeg een geplande wijziging toe
        var scheduledChange = new ScheduledDishChange(
                dish.getId(),
                LocalDateTime.now().plusMinutes(10),
                DishState.PUBLISHED,
                "Nieuwe naam",
                "Nieuwe beschrijving",
                new BigDecimal("12.00"),
                25
        );
        scheduledDishChangeRepository.save(scheduledChange);

        // Act
        var overview = restaurantService.getOverviewForRestaurantAndOwner(
                restaurant.getId().id(),
                ownerId
        );

        // Assert
        assertThat(overview).isNotNull();
        assertThat(overview.pendingChanges()).isNotEmpty();
        assertThat(overview.pendingChanges().get(0).targetName()).isEqualTo("Nieuwe naam");
    }
    @Test
    void checkout_shouldSucceedWhenDishAndRestaurantAreValid() {
        // Arrange
        Restaurant restaurant = testHelper.saveRestaurent();
        DayOfWeek today = LocalDate.now().getDayOfWeek();

        restaurantService.addOpenhours(
                restaurant.getId().id(),
                LocalTime.of(23, 59),
                LocalTime.of(0, 0),
                today
        );

        CreateDishCommand dishCommand = new CreateDishCommand(
                restaurant.getId().id(),
                "Test Burger",
                "Juicy test burger",
                new BigDecimal("10.00"),
                DishState.PUBLISHED,
                15
        );
        Dish dish = restaurantService.createDish(dishCommand);

        var orderLine = new CheckoutRequestDto.OrderLineDto(
                dish.getId().id(),
                dish.getName(),
                dish.getPrice(),
                1,
                dish.getPreparationTime()
        );

        var request = new CheckoutRequestDto(
                UUID.randomUUID(),              // orderId
                restaurant.getId().id(),        // restaurantId
                UUID.randomUUID(),              // clientId
                List.of(orderLine)
        );

        dish.changeStateTo(DishState.PUBLISHED);

        // Act
        CheckOutResponseCommand response = checkoutService.checkout(CheckOutRequestCommand.from(request));

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.success()).isTrue();
        assertThat(response.message()).isEqualTo("Checkout succesvol");
    }
}