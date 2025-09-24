package be.kdg.sa.restaurantservice.infrastructure.restaurant.jpa;

import be.kdg.sa.restaurantservice.domain.Owner.Owner;
import be.kdg.sa.restaurantservice.domain.Restaurant.Restaurant;
import be.kdg.sa.restaurantservice.domain.Restaurant.RestaurantType;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.List;
import java.util.UUID;


@Entity
@Getter
@Table(name = "restaurant")
public class JpaRestaurantEntity {
    @Id
    private UUID id;
    @Column(nullable = false)
    private UUID ownerId;


    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String logo;
    @Column(nullable = false)
    private String email;
    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private RestaurantType type;

    @OneToMany(mappedBy = "restaurant",cascade = CascadeType.ALL ,fetch = FetchType.LAZY, orphanRemoval = true)
    private List<JpaDishEntity> dishes;

    protected JpaRestaurantEntity() {}

    public JpaRestaurantEntity(UUID id, UUID ownerId, String name,String email, String logo, RestaurantType type) {
        this.id = id;
        this.ownerId = ownerId;
        this.name = name;
        this.logo = logo;
        this.type = type;
        this.email = email;
    }

    public static JpaRestaurantEntity fromDomain(Restaurant restaurant) {
        JpaRestaurantEntity jpaRestaurantEntity = new JpaRestaurantEntity(restaurant.getId().id(), restaurant.getOwnerId().id(),
                restaurant.getName(), restaurant.getEmail(), restaurant.getLogo(), restaurant.getType());

        List<JpaDishEntity> jpaDishEntity = restaurant.getDishes().stream()
                .map(JpaDishEntity::fromDomain)
                .toList();

        jpaRestaurantEntity.setDishes(jpaDishEntity);

        return jpaRestaurantEntity;
    }

    public void setDishes(List<JpaDishEntity> dishes) {
        this.dishes = dishes;
        this.dishes.forEach(dish -> dish.setRestaurant(this));
    }

}
