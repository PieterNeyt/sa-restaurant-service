package be.kdg.sa.restaurantservice.infrastructure.jpa;

import be.kdg.sa.restaurantservice.domain.restaurant.dish.Dish;
import be.kdg.sa.restaurantservice.domain.restaurant.dish.DishId;
import be.kdg.sa.restaurantservice.domain.restaurant.dish.DishState;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;


@Entity
@Getter
@Table(name = "dish")
public class JpaDishEntity {
    @Id
    private UUID id;
    @Setter
    @Column(nullable = false)
    private String name;
    @Setter
    @Column(nullable = false)
    private String description;
    @Setter
    @Column(nullable = false)
    private BigDecimal price;

    @Setter
    @Column(nullable = false)
    private int preparationTime;
    @Setter
    @Enumerated(EnumType.STRING)


    @Column(nullable = false)
    private DishState state;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "restaurant_id")
    private JpaRestaurantEntity restaurant;





    protected JpaDishEntity() {}

    public JpaDishEntity(UUID id, String name, String description, BigDecimal price, DishState state, int  preparationTime) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.state = state;
        this.preparationTime = preparationTime;
    }

    public static JpaDishEntity fromDomain(Dish dish) {
        return new JpaDishEntity(
                dish.getId().id(),
                dish.getName(),
                dish.getDescription(),
                dish.getPrice(),
                dish.getState(),
                dish.getPreparationTime()
        );
    }

    public Dish toDomain() {
        return new Dish(new DishId(id),state,name,description,price,preparationTime);
    }

}
