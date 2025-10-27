package be.kdg.sa.restaurantservice.domain;

import java.util.Optional;

public interface SecurityCatalog {
    Optional<String> getAccesToken();
}
