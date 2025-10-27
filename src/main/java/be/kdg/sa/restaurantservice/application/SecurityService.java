package be.kdg.sa.restaurantservice.application;

import be.kdg.sa.restaurantservice.domain.NotFoundException;
import be.kdg.sa.restaurantservice.domain.SecurityCatalog;
import org.springframework.stereotype.Service;

@Service
public class SecurityService {
    private final SecurityCatalog securityCatalog;
    public SecurityService(SecurityCatalog securityCatalog) {
        this.securityCatalog = securityCatalog;
    }
    public String getJwtAccesToken() {
        return securityCatalog.getAccesToken()
                .orElseThrow(() -> new NotFoundException("Acces token not found"));
    }
}
