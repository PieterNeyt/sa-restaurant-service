package be.kdg.sa.restaurantservice.infrastructure.restaurant;

import be.kdg.sa.restaurantservice.domain.Restaurant.ScheduledDishChange;
import be.kdg.sa.restaurantservice.domain.Restaurant.ScheduledDishChangeRepository;
import be.kdg.sa.restaurantservice.infrastructure.restaurant.jpa.JpaScheduledDishChangeEntity;
import be.kdg.sa.restaurantservice.infrastructure.restaurant.jpa.JpaScheduledDishChangeRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

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


}
