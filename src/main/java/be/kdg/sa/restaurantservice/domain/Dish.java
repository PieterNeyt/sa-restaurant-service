package be.kdg.sa.restaurantservice.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.jmolecules.ddd.annotation.AggregateRoot;
import org.jmolecules.ddd.annotation.Entity;

import java.util.UUID;

@Entity
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

