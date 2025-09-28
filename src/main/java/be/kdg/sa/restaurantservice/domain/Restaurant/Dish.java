package be.kdg.sa.restaurantservice.domain.Restaurant;

import lombok.Getter;
import lombok.Setter;
import org.jmolecules.ddd.annotation.Entity;

import java.math.BigDecimal;

@Entity
@Getter
public class Dish {
    private final DishId id;
    @Setter
    private String name;
    @Setter
    private String description;
    @Setter
    private BigDecimal price;
    @Setter
    private DishState state;

    public Dish(String name, String description, BigDecimal price) {
        this.id = DishId.create();
        this.name = name;
        this.description = description;
        this.price = price;
        this.state = DishState.NOT_PUBLISHED;
    }
    public Dish(DishId id,DishState state, String name, String description, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.state = state;
    }
    public void changeNameTo(String newName) {
        if (newName == null || newName.isBlank()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        this.name = newName;
    }

    public void changeDescriptionTo(String newDescription) {
        this.description = newDescription;
    }

    public void changePriceTo(BigDecimal newPrice) {
        if (newPrice == null || newPrice.signum() < 0) {
            throw new IllegalArgumentException("Prijs moet groter zijn dan 0");
        }
        this.price = newPrice;
    }

}

