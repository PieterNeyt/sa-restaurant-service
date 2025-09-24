package be.kdg.sa.restaurantservice.domain.Restaurant;

import lombok.Getter;
import org.jmolecules.ddd.annotation.Entity;

import java.math.BigDecimal;

@Entity
@Getter
public class Dish {
    private final DishId id;
    private final String name;
    private final String description;
    private final BigDecimal price;
    private DishState state;

    public Dish(String name, String description, BigDecimal price) {
        this.id = DishId.create();
        this.name = name;
        this.description = description;
        this.price = price;
        this.state = DishState.PUBLISHED;
    }
    public Dish(DishId id,DishState state, String name, String description, BigDecimal price) {
        this.id = DishId.create();
        this.name = name;
        this.description = description;
        this.price = price;
        this.state = DishState.PUBLISHED;
    }
}

