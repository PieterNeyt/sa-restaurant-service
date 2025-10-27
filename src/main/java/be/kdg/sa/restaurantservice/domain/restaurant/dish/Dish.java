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
        Assert.notNull(price, "Price must not be null");

        Assert.hasText(name, "Name must not be blank");
        Assert.hasText(description, "Description must not be blank");

        Assert.isTrue(price.compareTo(BigDecimal.ZERO) > 0, "Price must be greater than zero");
        Assert.isTrue(preparationTime > 0, "Preperationtime must be greater than zero");

        this.id = DishId.create();
        this.name = name;
        this.description = description;
        this.price = price;
        this.state = state;
        this.preparationTime = preparationTime;
    }
    public Dish(DishId id,DishState state, String name, String description, BigDecimal price,  int preparationTime) {
        Assert.notNull(id, "DishId must not be null");
        Assert.notNull(state, "DishState must not be null");
        Assert.notNull(price, "Price must not be null");

        Assert.hasText(name, "Name must not be blank");
        Assert.hasText(description, "Description must not be blank");

        Assert.isTrue(price.compareTo(BigDecimal.ZERO) > 0, "Price must be greater than zero");
        Assert.isTrue(preparationTime > 0, "Preperationtime must be greater than zero");

        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.state = state;
        this.preparationTime = preparationTime;
    }
    public void changeNameTo(String newName) {
        if (newName == null || newName.isBlank()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        this.name = newName;
    }
    public void changePreparationTime(int newPreparationTime) {
        if (newPreparationTime <0) {
            throw new IllegalArgumentException("Preperationtime cannot be smaller than 0");
        }
        this.preparationTime = newPreparationTime;
    }

    public void changeDescriptionTo(String newDescription) {
        this.description = newDescription;
    }

    public void changePriceTo(BigDecimal newPrice) {
        if (newPrice == null || newPrice.signum() < 0) {
            throw new IllegalArgumentException("price must be higher than 0");
        }
        this.price = newPrice;
    }

    public void changeStateTo(DishState targetState) {
        if(targetState == null) {
            throw new IllegalArgumentException("State must not be null");
        }
        this.state = targetState;
    }


}

