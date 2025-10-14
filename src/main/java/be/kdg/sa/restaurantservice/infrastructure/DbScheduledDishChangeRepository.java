package be.kdg.sa.restaurantservice.infrastructure;

import be.kdg.sa.restaurantservice.domain.schedulechange.ScheduledDishChange;
import be.kdg.sa.restaurantservice.domain.schedulechange.ScheduledDishChangeRepository;
import be.kdg.sa.restaurantservice.infrastructure.jpa.JpaScheduledDishChangeEntity;
import be.kdg.sa.restaurantservice.infrastructure.jpa.JpaScheduledDishChangeRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
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
    public List<ScheduledDishChange> findDueChangesByRestaurantAndOwner( UUID restaurantId, UUID ownerId) {
        return jpaRepo.findDueChangesForRestaurantAndOwner( restaurantId, ownerId)
                .stream()
                .map(JpaScheduledDishChangeEntity::toDomain)
                .toList();
    }


}
