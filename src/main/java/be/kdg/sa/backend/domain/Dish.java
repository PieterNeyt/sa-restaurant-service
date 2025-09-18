package be.kdg.sa.backend.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Getter
@Setter
@ToString
public class Dish {
    public UUID dishId;
    public String name;
    public DishState state;
    public Boolean price;
    public String description;

    public Dish(String description, DishState state, Boolean price, String name) {
        this.description = description;
        this.state = state;
        this.price = price;
        this.name = name;
    }
}
