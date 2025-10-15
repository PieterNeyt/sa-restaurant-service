package be.kdg.sa.restaurantservice.application;

import be.kdg.sa.restaurantservice.TestHelper;
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
    private TestHelper testHelper;

    @AfterEach
    void cleanup() {
        testHelper.cleanUp();
        scheduledDishChangeRepository.deleteAll();
    }

    @Test
    void createRestaurant_shouldPersistRestaurantWithAllDetails() {
        // Arrange
        UUID ownerId = UUID.randomUUID();
        UUID addressId = UUID.randomUUID();
        CreateRestaurantCommand command = new CreateRestaurantCommand(
                ownerId,
                addressId,
                RestaurantType.PIZZERIA,
                "Pizza Palace",
                "info@pizzapalace.com",
                "palace-logo.png",
                List.of()
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
        UUID addressId = UUID.randomUUID();
        CreateRestaurantCommand command = new CreateRestaurantCommand(
                ownerId,
                addressId,
                RestaurantType.BUFFET,
                "First Restaurant",
                "first@test.com",
                "logo1.png",
                List.of()
        );

        // Create first restaurant
        restaurantService.createRestaurant(command);

        // Create second command with same owner
        CreateRestaurantCommand duplicateCommand = new CreateRestaurantCommand(
                ownerId,
                UUID.randomUUID(),
                RestaurantType.SEAFOOD,
                "Second Restaurant",
                "second@test.com",
                "logo2.png",
                List.of()
        );

        // Act & Assert
        assertThatThrownBy(() -> restaurantService.createRestaurant(duplicateCommand))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Owner already owns restaurant");
    }

    @Test
    void createDishAndUpdateState_shouldPersistDishAndUpdateCorrectly() {
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

        // Act - Create dish
        Dish createdDish = restaurantService.createDish(dishCommand);

        // Assert - Verify creation
        assertThat(createdDish).isNotNull();
        assertThat(createdDish.getName()).isEqualTo("Margherita Pizza");
        assertThat(createdDish.getState()).isEqualTo(DishState.NOT_PUBLISHED);

        // Act - Update state
        restaurantService.updateStateDish(createdDish.getId().id(), DishState.PUBLISHED);

        // Assert - Verify update
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

        // Create scheduled change in the past (so it's immediately due)
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

        // Act - Execute scheduler
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
    void addOpeningHoursAndToggleRestaurantState_shouldPersistCorrectly() {
        // Arrange
        UUID ownerId = UUID.randomUUID();
        UUID addressId = UUID.randomUUID();
        CreateRestaurantCommand command = new CreateRestaurantCommand(
                ownerId,
                addressId,
                RestaurantType.PIZZERIA,
                "Trattoria Roma",
                "roma@test.com",
                "roma-logo.png",
                List.of()
        );
        Restaurant restaurant = restaurantService.createRestaurant(command);

        // Act - Add opening hours
        OpeningHour mondayHours = restaurantService.addOpenhours(
                restaurant.getId().id(),
                LocalTime.of(22, 0),
                LocalTime.of(11, 0),
                DayOfWeek.MONDAY
        );

        OpeningHour fridayHours = restaurantService.addOpenhours(
                restaurant.getId().id(),
                LocalTime.of(23, 30),
                LocalTime.of(11, 0),
                DayOfWeek.FRIDAY
        );

        // Assert - Verify opening hours
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
}