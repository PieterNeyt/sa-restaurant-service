package be.kdg.sa.restaurantservice.infrastructure.securitycatalog;

import be.kdg.sa.restaurantservice.domain.SecurityCatalog;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class ExternalSecurityCatalog implements SecurityCatalog {

    private final RestClient restClient;

    @Value("${security.oauth.client-id}")
    private String clientId;

    @Value("${security.oauth.client-secret}")
    private String clientSecret;

    @Value("${security.oauth.username}")
    private String username;

    @Value("${security.oauth.password}")
    private String password;

    @Value("${security.oauth.grant-type}")
    private String grantType;

    @Value("${security.oauth.scope}")
    private String scope;

    public ExternalSecurityCatalog(@Qualifier("SecurityCatalogApi") final RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public Optional<String> getAccesToken() {

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("client_id", clientId);
        formData.add("client_secret", clientSecret);
        formData.add("username", username);
        formData.add("password", password);
        formData.add("grant_type", grantType);
        formData.add("scope", scope);

        Map<String, Object> response = restClient
                .post()
                .uri("")
                .body(formData)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});

        String accessToken = response != null ? (String) response.get("access_token") : null;
        return Optional.ofNullable(accessToken);
    }
}