# Spring Security JWT Authentication Flow Documentation

## Plan / Checklist

- Summarize the sign-in HTTP flow end-to-end.
- Show where authentication happens in code and how JWT is generated (file + method references).
- Explain how subsequent requests are authenticated using the JWT (filter + verification).
- Show sample HTTP request/response and point out where keys/expiration come from.
- Provide a couple of short notes/possible improvements.

---

# End-to-End Sign-In Flow (High Level)

1. Client POSTs credentials to the sign-in endpoint:

```http
POST /api/auth/public/signin
```

2. The controller authenticates username/password using Spring Security (`AuthenticationManager`).

3. If authentication succeeds, the app generates a signed JWT (RSA private key) containing:
    - Subject (`username`)
    - Roles
    - Tenant ID
    - Issued timestamp
    - Expiration timestamp

4. The controller returns the JWT in the response body (`LoginResponse`).

5. For subsequent requests the client sends the JWT in the `Authorization` header:

```http
Authorization: Bearer <token>
```

6. A servlet filter (`AuthTokenFilter`) intercepts each request:
    - Extracts the JWT
    - Validates the signature using the RSA public key
    - Loads the user details
    - Sets the `SecurityContext`

7. Spring Security authorizes the request.

---

# Detailed Code-Centered Explanation

# 1. Sign-In Endpoint (Where the Client Calls)

## File

```text
src/main/java/com/infyniteloop/isec/security/controller/AuthController.java
```

## Method

```java
authenticateUser(...)
```

Mapped using:

```java
@PostMapping("/public/signin")
```

## Responsibilities

- Receives a `LoginRequest` containing:
    - username
    - password

- Calls:

```java
authenticationManager.authenticate(
    new UsernamePasswordAuthenticationToken(...)
)
```

- If authentication fails:
    - `AuthenticationException` is caught
    - Error response is returned

- If authentication succeeds:
    - Stores authentication in `SecurityContextHolder`
    - Extracts `UserDetails`
    - Generates JWT using:

```java
JwtUtils.generateTokenFromUsername(userDetails)
```

- Returns:

```java
LoginResponse(username, roles, jwtToken)
```

---

# 2. How Username/Password Authentication Works

## UserDetails Lookup

### File

```text
src/main/java/com/infyniteloop/isec/security/services/UserDetailsServiceImpl.java
```

### Method

```java
loadUserByUsername(String username)
```

### Responsibilities

- Loads the `User` entity from database using `UserRepository`
- Returns:

```java
UserDetailsImpl.build(user)
```

---

## Password Verification

Spring Security compares:

- Password from `LoginRequest`
- Stored encrypted password

Using:

```java
BCryptPasswordEncoder
```

### Configuration File

```text
src/main/java/com/infyniteloop/isec/security/SecurityConfig.java
```

### Bean Definition

```java
@Bean
public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
}
```

---

# 3. JWT Generation

## File

```text
src/main/java/com/infyniteloop/isec/security/jwt/JwtUtils.java
```

## Method

```java
generateTokenFromUsername(UserDetails userDetails)
```

## JWT Creation Flow

- Loads full `User` from DB using:

```java
userRepository.findByUserName(...)
```

This is done to include:
- `tenantId`
- `roles`

inside JWT claims.

---

## JWT Claims

| Claim | Description |
|---|---|
| `sub` | username |
| `roles` | user roles |
| `tenantId` | tenant identifier |
| `iat` | issued timestamp |
| `exp` | expiration timestamp |

---

## JWT Signing

The token is signed using the RSA private key:

```java
.signWith(privateKey)
```

---

# 4. JWT Key Management

## Configuration Files

```text
src/main/java/com/infyniteloop/isec/security/config/JwtConfig.java
```

```text
src/main/java/com/infyniteloop/isec/security/util/JwtKeyLoader.java
```

---

## Environment Variables

Keys are loaded from:

```text
JWT_PRIVATE_KEY
JWT_PUBLIC_KEY
```

(Base64 encoded)

---

# 5. JWT Expiration Configuration

## Property

```properties
spring.app.jwtExpirationMs
```

## File

```text
src/main/resources/application.properties
```

### Example

```properties
spring.app.jwtExpirationMs=172800000
```

Equivalent to:

```text
2 days
```

---

# 6. Login Response Format

## DTO File

```text
src/main/java/com/infyniteloop/isec/security/dtos/LoginResponse.java
```

## Example Response

```json
{
  "username": "alice",
  "roles": ["ROLE_USER"],
  "jwtToken": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

---

# 7. JWT Validation for Subsequent Requests

## Filter

### File

```text
src/main/java/com/infyniteloop/isec/security/jwt/AuthTokenFilter.java
```

### Class

```java
extends OncePerRequestFilter
```

---

## JWT Extraction

Reads:

```http
Authorization: Bearer <token>
```

Using:

```java
jwtUtils.getJwtFromHeader(request)
```

---

## Token Validation

### Method

```java
validateJwtToken(jwt)
```

Internally uses:

```java
Jwts.parser()
    .verifyWith(publicKey)
    .build()
    .parseSignedClaims(authToken)
```

---

## Exceptions Handled

- `MalformedJwtException`
- `ExpiredJwtException`
- Invalid signature
- Other parsing exceptions

If validation fails:
- Returns `false`
- Request proceeds unauthenticated

---

# 8. SecurityContext Population

If token is valid:

1. Extract username:

```java
jwtUtils.getUserNameFromJwtToken(jwt)
```

2. Reload user details from DB:

```java
UserDetailsServiceImpl.loadUserByUsername(username)
```

3. Create authentication object:

```java
UsernamePasswordAuthenticationToken
```

4. Store authentication inside:

```java
SecurityContextHolder
```

This allows Spring Security authorization to work correctly.

---

# 9. Roles and Authorities Handling

JWT contains:

- `roles`
- `tenantId`

However:

> Authorities are NOT taken directly from the JWT.

Instead:

- User details are reloaded from DB
- Authorities come from `UserDetailsImpl`

---

## Benefit of This Approach

Role changes in DB take effect immediately without waiting for JWT expiration.

This keeps:
- Database = source of truth
- JWT = authentication proof

---

# 10. 2FA Flow (Important)

## Current Implementation

JWT is issued immediately after username/password authentication.

Separate endpoint:

```http
POST /api/auth/public/verify-2fa-login
```

Validates:
- JWT
- TOTP code

---

## Important Security Note

Current implementation allows JWT generation before 2FA verification.

### Stronger Alternative

Recommended improvement:

- Issue temporary pre-auth token first
- Issue full access token only after successful 2FA verification

---

# Sample API Usage

# Sign In Request

```bash
curl -X POST http://localhost:8080/api/auth/public/signin \
-H "Content-Type: application/json" \
-d '{"username":"alice","password":"secret"}'
```

---

# Sample Login Response

```json
{
  "username": "alice",
  "roles": ["ROLE_USER"],
  "jwtToken": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

---

# Access Protected API

```bash
curl http://localhost:8080/api/notes \
-H "Authorization: Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9..."
```

---

# Where Keys and Properties Come From

# Keys

`JwtKeyLoader` expects:

```text
JWT_PRIVATE_KEY
JWT_PUBLIC_KEY
```

Environment variables containing Base64 encoded keys.

`JwtConfig` exposes:
- `PrivateKey`
- `PublicKey`

as Spring beans.

---

# application.properties

Application also contains:

```properties
spring.app.jwt.privateKeyPath
spring.app.jwt.publicKeyPath
```

However current implementation uses environment variables instead.

---

# Expiration

Configured using:

```properties
spring.app.jwtExpirationMs
```

Example:

```properties
spring.app.jwtExpirationMs=172800000
```

Equivalent to:
- 48 hours
- 2 days

---

# Security / Implementation Notes

# 1. Improve 2FA Enforcement

Current flow:
- JWT issued before TOTP validation

Recommended:
- Issue short-lived temporary token first
- Issue full JWT only after successful 2FA

---

# 2. DB Reload for Authorities

Current implementation reloads authorities from DB after validating JWT.

## Advantages

- Immediate role revocation
- Centralized authorization control
- Keeps DB as source of truth

---

## Alternative

Could reconstruct authorities from JWT claims for fully stateless authorization.

Trade-offs:
- Harder to revoke permissions immediately
- More security complexity

---

# 3. Refresh Tokens

Consider implementing refresh tokens to:
- Avoid frequent re-login
- Support long-lived sessions securely

---

# 4. Token Revocation Strategy

Consider:
- Blacklist
- Whitelist
- Logout invalidation strategy

for immediate token revocation support.

---

# 5. Null Safety Improvement

Current implementation assumes:

```java
user.getTenantId() != null
```

Potential issue:

```java
tenantId.toString()
```

could throw:

```text
NullPointerException
```

Recommended improvement:

```java
if (user.getTenantId() != null)
```

before adding claim.

---