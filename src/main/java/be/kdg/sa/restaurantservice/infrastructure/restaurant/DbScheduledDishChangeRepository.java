package be.kdg.sa.restaurantservice.infrastructure.restaurant;

import be.kdg.sa.restaurantservice.domain.restaurant.dish.Dish;
import be.kdg.sa.restaurantservice.domain.restaurant.dish.DishId;
import be.kdg.sa.restaurantservice.domain.schedulechange.ScheduledDishChange;
import be.kdg.sa.restaurantservice.domain.schedulechange.ScheduledDishChangeRepository;
import be.kdg.sa.restaurantservice.infrastructure.restaurant.jpa.JpaDishEntity;
import be.kdg.sa.restaurantservice.infrastructure.restaurant.jpa.JpaScheduledDishChangeEntity;
import be.kdg.sa.restaurantservice.infrastructure.restaurant.jpa.JpaScheduledDishChangeRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class DbScheduledDishChangeRepository implements ScheduledDishChangeRepository {
    private final JpaScheduledDishChangeRepository jpaRepo;

    public DbScheduledDishChangeRepository(JpaScheduledDishChangeRepository jpaRepo) {
        this.jpaRepo = jpaRepo;
    }

    @Override
    public void save(ScheduledDishChange change) {
        jpaRepo.save(JpaScheduledDishChangeEntity.fromDomain(change));
    }

    @Override
    public List<ScheduledDishChange> findDueChanges(LocalDateTime now) {
        return jpaRepo.findByScheduledTimeBefore(now)
                .stream()
                .map(JpaScheduledDishChangeEntity::toDomain)
                .toList();
    }

    @Override
    public void delete(ScheduledDishChange change) {
        jpaRepo.deleteById(change.getId().id());
    }

    @Override
    public void deleteAll() {
        jpaRepo.deleteAll();
    }



    @Override
    public List<ScheduledDishChange> findDueChangesByRestaurantAndOwner( UUID restaurantId, UUID ownerId) {
        return jpaRepo.findDueChangesForRestaurantAndOwner( restaurantId, ownerId)
                .stream()
                .map(JpaScheduledDishChangeEntity::toDomain)
                .toList();
    }


}
