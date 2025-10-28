package be.kdg.sa.restaurantservice.domain.restaurant.dish;

import lombok.Getter;
import org.jmolecules.ddd.annotation.Entity;
import org.jmolecules.ddd.annotation.Identity;
import org.springframework.util.Assert;

import java.math.BigDecimal;

@Entity
@Getter
public class Dish {
    @Identity
    private final DishId id;
    private String name;
    private String description;
    private BigDecimal price;
    private DishState state;
    private int preparationTime;

    public Dish(String name, String description, BigDecimal price, int preparationTime, DishState state) {
        this.id = DishId.create();
        this.name = name;
        this.description = description;
        this.price = price;
        this.state = state;
        this.preparationTime = preparationTime;
    }
    public Dish(DishId id,DishState state, String name, String description, BigDecimal price,  int preparationTime) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.state = state;
        this.preparationTime = preparationTime;
    }
    public void changeNameTo(String newName) {
        if (newName == null || newName.isBlank()) {
            throw new ActionNotPossibleException("Name cannot be empty");
        }
        this.name = newName;
    }
    public void changePreparationTime(int newPreparationTime) {
        if (newPreparationTime <0) {
            throw new ActionNotPossibleException("Preperationtime cannot be smaller than 0");
        }
        this.preparationTime = newPreparationTime;
    }

    public void changeDescriptionTo(String newDescription) {
        this.description = newDescription;
    }

    public void changePriceTo(BigDecimal newPrice) {
        if (newPrice == null || newPrice.signum() < 0) {
            throw new ActionNotPossibleException("price must be higher than 0");
        }
        this.price = newPrice;
    }

    public void publish() {
        this.state = DishState.PUBLISHED;
    }
    public void markTempNotAvailable() {
        this.state = DishState.TEMP_NOT_AVAILABLE;
    }
    public void hide() {
        this.state = DishState.NOT_PUBLISHED;
    }

}

