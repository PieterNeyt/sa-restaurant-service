package be.kdg.sa.restaurantservice.infrastructure.jpa;

import org.jmolecules.ddd.annotation.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

@Repository
public interface JpaDishRepository extends JpaRepository<JpaDishEntity, UUID> {

}