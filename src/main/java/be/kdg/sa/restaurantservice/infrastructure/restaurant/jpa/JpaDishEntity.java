package be.kdg.sa.restaurantservice.infrastructure.restaurant.jpa;

import be.kdg.sa.restaurantservice.domain.Restaurant.Dish;
import be.kdg.sa.restaurantservice.domain.Restaurant.DishState;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.UUID;


@Entity
@Getter
@Table(name = "dish")
public class JpaDishEntity {
    @Id
    private UUID id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private boolean price;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DishState state;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "restaurant_id")
    private JpaRestaurantEntity restaurant;

    protected JpaDishEntity() {}

    public JpaDishEntity(UUID id, String name, String description, boolean price, DishState state) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.state = state;
    }

    static JpaDishEntity fromDomain(Dish dish) {
        return new JpaDishEntity(
                dish.getId().id(),
                dish.getName(),
                dish.getDescription(),
                dish.isPrice(),
                dish.getState()
        );
    }

    public void setRestaurant(JpaRestaurantEntity jpaRestaurantEntity) {
        this.restaurant = jpaRestaurantEntity;
    }
}
