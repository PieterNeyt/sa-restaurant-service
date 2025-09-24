package be.kdg.sa.restaurantservice.domain.Restaurant;

import lombok.Getter;
import org.jmolecules.ddd.annotation.Entity;

@Entity
@Getter
public class Dish {
    private final DishId id;
    private final String name;
    private final String description;
    private final boolean price;
    private DishState state;

    public Dish(String name, String description, boolean price) {
        this.id = DishId.create();
        this.name = name;
        this.description = description;
        this.price = price;
        this.state = DishState.AVAILABLE;
    }
}

