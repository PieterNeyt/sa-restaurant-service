package be.kdg.sa.restaurantservice.infrastructure.jpa;

import be.kdg.sa.restaurantservice.domain.owner.OwnerId;
import be.kdg.sa.restaurantservice.domain.restaurant.*;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
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

    @Embedded
    private JpaAddressEmbeddable address;


    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String logo;
    @Column(nullable = false)
    private String email;
    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private RestaurantType type;
    @Column(nullable = false)
    private boolean isOpen;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PriceCategory priceCategory;


    @OneToMany(mappedBy = "restaurant",cascade = CascadeType.ALL ,fetch = FetchType.LAZY, orphanRemoval = true)
    private List<JpaDishEntity> dishes= new ArrayList<>();;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "restaurant_opening_hours", joinColumns = @JoinColumn(name = "restaurant_id"))
    private List<JpaOpeningHourEntity> openingHours= new ArrayList<>();;


    protected JpaRestaurantEntity() {}

    public JpaRestaurantEntity(UUID id, UUID ownerId,JpaAddressEmbeddable  address, String name,String email, String logo, RestaurantType type, PriceCategory priceCategory) {
        this.id = id;
        this.ownerId = ownerId;
        this.name = name;
        this.logo = logo;
        this.type = type;
        this.email = email;
        this.address = address;
        this.isOpen = false;
        this.priceCategory = priceCategory;
    }

    public static JpaRestaurantEntity fromDomain(Restaurant restaurant) {
        JpaRestaurantEntity jpaRestaurantEntity = new JpaRestaurantEntity(
                restaurant.getId().id(),
                restaurant.getOwnerId().id(),
                JpaAddressEmbeddable.fromDomain(restaurant.getAddress()),
                restaurant.getName(),
                restaurant.getEmail(),
                restaurant.getLogo(),
                restaurant.getType(),
                restaurant.getPriceCategory());
        jpaRestaurantEntity.isOpen = restaurant.isOpen();
        jpaRestaurantEntity.priceCategory = restaurant.getPriceCategory();

        List<JpaDishEntity> jpaDishEntity = restaurant.getDishes().stream()
                .map(JpaDishEntity::fromDomain)
                .toList();

        jpaRestaurantEntity.setOpeningHours(
                restaurant.getOpeningHours().stream()
                        .map(JpaOpeningHourEntity::fromDomain)
                        .toList()
        );

        jpaRestaurantEntity.setDishes(jpaDishEntity);

        return jpaRestaurantEntity;
    }



    public Restaurant toDomain() {
        Restaurant restaurant = new Restaurant(
                new RestaurantId(id),
                new OwnerId(ownerId),
                address != null ? address.toDomain() : null,
                type,
                name,
                email,
                logo,
                 isOpen,
                priceCategory);

        dishes.forEach(dish -> restaurant.addDish(
                dish.getId(),
                dish.getDescription(),
                dish.getName(),
                dish.getState(),
                dish.getPrice(),
                dish.getPreparationTime())
        );
        openingHours.forEach(oh ->
                restaurant.addOpeningHour(
                        oh.getDayOfWeek(),
                        oh.getOpeningTime(),
                        oh.getClosingTime())
        );

        return restaurant;
    }

    public void setDishes(List<JpaDishEntity> dishes) {
        this.dishes = dishes;
        this.dishes.forEach(dish -> dish.setRestaurant(this));
    }
    private void setOpeningHours(List<JpaOpeningHourEntity> list) {
        this.openingHours = list;
    }

}
