package be.kdg.sa.restaurantservice.infrastructure.restaurant.jpa;

import be.kdg.sa.restaurantservice.domain.address.AddressId;
import be.kdg.sa.restaurantservice.domain.owner.OwnerId;
import be.kdg.sa.restaurantservice.domain.restaurant.*;
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
    @Column(nullable = true)
    private UUID addresId;


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
    private List<JpaDishEntity> dishes;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "restaurant_opening_hours", joinColumns = @JoinColumn(name = "restaurant_id"))
    private List<JpaOpeningHourEntity> openingHours;


    protected JpaRestaurantEntity() {}

    public JpaRestaurantEntity(UUID id, UUID ownerId,UUID addresId, String name,String email, String logo, RestaurantType type) {
        this.id = id;
        this.ownerId = ownerId;
        this.name = name;
        this.logo = logo;
        this.type = type;
        this.email = email;
        this.addresId=addresId;
        this.isOpen = false;
    }

    public static JpaRestaurantEntity fromDomain(Restaurant restaurant) {
        JpaRestaurantEntity jpaRestaurantEntity = new JpaRestaurantEntity(
                restaurant.getId().id(),
                restaurant.getOwnerId().id(),
                restaurant.getAddressId().id(),
                restaurant.getName(),
                restaurant.getEmail(),
                restaurant.getLogo(),
                restaurant.getType());
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
                new AddressId(addresId),
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
                dish.getPrice())
        );
        openingHours.forEach(oh -> restaurant.addOpeningHour(
                new OpeningHour(
                        oh.getDayOfWeek(),
                        oh.getOpeningTime(),
                        oh.getOpeningTime()))
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
