## 8. Test the Setup

### Get Access Token
```bash
curl -X POST http://localhost:8180/realms/keepdishesgoing/protocol/openid-connect/token \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "grant_type=password" \
  -d "client_id=backend-client" \
  -d "client_secret=NHhC580gogEpJ5K9fqsiyLiqF4VpqTKE" \
  -d "username=owner" \
  -d "password=owner"
```

### Use Token with Your API
```bash
curl -H "Authorization: Bearer YOUR_ACCESS_TOKEN" \
  http://localhost:8080/secured/admin
```

## 9. Role Mapping in JWT

By default, realm roles appear in the JWT token under the `realm_access.roles` claim. To map them to Spring Security authorities, configure your JWT decoder or use a custom `JwtAuthenticationConverter`.

Example JWT payload:
```json
{
  "sub": "user-id",
  "given_name": "owner",
  "family_name": "owner",
  "email": "owner@example.com",
  "realm_access": {
    "roles": ["owner"]
  }
}
```