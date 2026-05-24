# Sign-in + JWT Validation Flow

## 1. SIGN-IN FLOW (POST /api/auth/public/signin)

```
┌──────────┐                  ┌─────────────────┐         ┌──────────────┐
│  Client  │                  │  AuthController │         │ Spring       │
│          │                  │                 │         │ Security     │
└────┬─────┘                  └────────┬────────┘         └──────┬───────┘
     │                                 │                         │
     │ 1. POST /signin                 │                         │
     │ {username, password}            │                         │
     ├────────────────────────────────>│                         │
     │                                 │                         │
     │                                 │ 2. authenticateUser()   │
     │                                 │    receives LoginRequest│
     │                                 │                         │
     │                                 │ 3. AuthenticationManager│
     │                                 │    .authenticate(       │
     │                                 │    UsernamePassword     │
     │                                 │    AuthenticationToken) │
     │                                 ├────────────────────────>│
     │                                 │                         │
     │                                 │                         │
     ├─────────────┐                   ├──────────────────────┐  │
     │ (in Memory) │   4. UserDetails  │  (in Memory)         │  │
     │ DB Query    │<──   ServiceImpl   │  Password Encoding   │  │
     │ findByUser- │     .loadUserBy   │  (BCryptPassword     │  │
     │ Name(user)  │     Username()    │   Encoder)           │  │
     │             │                   │     compares          │  │
     │ Returns:    │                   │  stored vs provided   │  │
     │ User entity │                   │                       │  │
     │ with roles, │                   │                       │  │
     │ tenantId,   │                   │                       │  │
     │ runningRoom│                    │                       │  │
     └─────────────┘                   └──────────────────────┘  │
                                       │                         │
                                       │ 5a. If password INVALID │
                                       │    throw AuthException  │
                                       │<────────────────────────┤
                                       │                         │
                 (on success)          │ 5b. Return Authentication
                                       │    object (authenticated)
                                       │<────────────────────────┤
     │                                 │<────────────────────────┤
     │                                 │ 6. AuthenticationManager│
     │                                 │    done, return Auth    │
     │                                 │                         │
     │                                 │ 7. userDetails =        │
     │                                 │    authentication       │
     │                                 │    .getPrincipal()      │
     │                                 │    (UserDetailsImpl)     │
     │                                 │                         │
     │                                 ├──────────────────────┐  │
     │                                 │                      │  │
     │                                 │ 8. jwtToken =        │  │
     │                                 │    jwtUtils          │  │
     │                                 │    .generate         │  │
     │                                 │    TokenFromUsername │  │
     │                                 │    (userDetails)     │  │
     │                                 │                      │  │
     │                                 │ 9. JWT CREATED:      │  │
     │                                 │    ┌────────────────┐│  │
     │                                 │    │ HEADER:        ││  │
     │                                 │    │ alg: RS256     ││  │
     │                                 │    │ typ: JWT       ││  │
     │                                 │    │                ││  │
     │                                 │    │ PAYLOAD:       ││  │
     │                                 │    │ subject:       ││  │
     │                                 │    │   username     ││  │
     │                                 │    │ roles: [...]   ││  │
     │                                 │    │ tenantId: uuid ││  │
     │                                 │    │ iat: now       ││  │
     │                                 │    │ exp: now+48hrs ││  │
     │                                 │    │                ││  │
     │                                 │    │ SIGNATURE:     ││  │
     │                                 │    │ RSA-SHA256     ││  │
     │                                 │    │ (privateKey)   ││  │
     │                                 │    └────────────────┘│  │
     │                                 │                      │  │
     │                                 └──────────────────────┘  │
     │                                 │                         │
     │                                 │ 10. Build LoginResponse │
     │                                 │     {                   │
     │                                 │       username,         │
     │                                 │       roles,            │
     │                                 │       jwtToken          │
     │                                 │     }                   │
     │                                 │                         │
     │ 11. HTTP 200 OK                 │                         │
     │ {jwtToken, username, roles}     │                         │
     │<────────────────────────────────┤                         │
     │                                 │                         │
```

---

## 2. SUBSEQUENT AUTHENTICATED REQUEST FLOW

```
┌──────────┐              ┌────────────────┐      ┌──────────────┐      ┌────────────────┐
│  Client  │              │ AuthTokenFilter│      │  JwtUtils    │      │ UserDetails    │
│          │              │                │      │              │      │ ServiceImpl     │
└────┬─────┘              └────────┬───────┘      └──────┬───────┘      └────────┬───────┘
     │                            │                     │                        │
     │ 1. GET /api/notes          │                     │                        │
     │ Authorization:             │                     │                        │
     │ Bearer eyJhbGc...          │                     │                        │
     ├───────────────────────────>│                     │                        │
     │                            │                     │                        │
     │                            │ 2. doFilterInternal │                        │
     │                            │    triggered        │                        │
     │                            │                     │                        │
     │                            │ 3. parseJwt(req)    │                        │
     │                            │    calls:           │                        │
     │                            │    getJwt           │                        │
     │                            │    FromHeader()     │                        │
     │                            ├────────────────────>│                        │
     │                            │                     │                        │
     │                            │                     │ 4. Extract from        │
     │                            │                     │    "Authorization"     │
     │                            │                     │    header, remove      │
     │                            │                     │    "Bearer " prefix    │
     │                            │                     │                        │
     │                            │ 5. jwt returned     │                        │
     │                            │<────────────────────┤                        │
     │                            │                     │                        │
     │                            │ 6. if (jwt != null) │                        │
     │                            │    validateJwt      │                        │
     │                            │    Token(jwt)       │                        │
     │                            ├────────────────────>│                        │
     │                            │                     │                        │
     │                            │                     │ 7. Parse signed claims│
     │                            │                     │    using RSA public    │
     │                            │                     │    key                 │
     │                            │                     │                        │
     │                            │                     │ 8. Verify signature    │
     │                            │                     │    and expiration      │
     │                            │                     │                        │
     │   ┌──────────────────────┐ │                     │                        │
     │   │ If validation FAILS: │ │                     │                        │
     │   │ - Catch exception    │ │                     │                        │
     │   │ - Log error          │ │<────────────────────┤                        │
     │   │ - Return: false      │ │                     │                        │
     │   │ - Request continues  │ │                     │                        │
     │   │   unauthenticated    │ │                     │                        │
     │   │   (fails at @Secured)│ │                     │                        │
     │   └──────────────────────┘ │                     │                        │
     │                            │                     │                        │
     │   ┌──────────────────────┐ │                     │                        │
     │   │ If validation OK:    │ │                     │ 9. Return: true        │
     │   │ (signature valid +   │ │                     │    (well-formed token)│
     │   │  not expired)        │ │<────────────────────┤                        │
     │   └──────────────────────┘ │                     │                        │
     │                            │                     │                        │
     │                            │ 10. Extract subject │                        │
     │                            │     (username)      │                        │
     │                            │     getUserName     │                        │
     │                            │     FromJwtToken()  │                        │
     │                            ├────────────────────>│                        │
     │                            │                     │                        │
     │                            │ 11. Parse claims,   │                        │
     │                            │     return subject  │                        │
     │                            │<────────────────────┤                        │
     │                            │                     │                        │
     │                            │ 12. username var    │                        │
     │                            │     now set         │                        │
     │                            │                     │                        │
     │                            │ 13. Load full user  │                        │
     │                            │     details from DB │                        │
     │                            │     loadUserBy      │                        │
     │                            │     Username()      │                        │
     │                            ├──────────────────────────────────────────────>│
     │                            │                     │                        │
     │                            │                     │     14. UserRepository│
     │                            │                     │         .findByUserName│
     │                            │                     │                        │
     │                            │                     │     15. Build          │
     │                            │                     │         UserDetailsImpl│
     │                            │                     │         with:          │
     │                            │                     │         - id           │
     │                            │                     │         - username     │
     │                            │                     │         - email        │
     │                            │                     │         - tenantId     │
     │                            │                     │         - runningRoomId│
     │                            │                     │         - location...  │
     │                            │                     │         - authorities  │
     │                            │                     │         (from roles)   │
     │                            │                     │                        │
     │                            │                     │ 16. UserDetailsImpl     │
     │                            │                     │     returned           │
     │                            │<──────────────────────────────────────────────┤
     │                            │                     │                        │
     │                            │ 17. Create          │                        │
     │                            │     UsernamePassword│                        │
     │                            │     AuthenticationT│                        │
     │                            │     oken(           │                        │
     │                            │       userDetails,  │                        │
     │                            │       null,         │                        │
     │                            │       authorities   │                        │
     │                            │     )               │                        │
     │                            │                     │                        │
     │                            │ 18. Set into        │                        │
     │                            │     SecurityContext │                        │
     │                            │     Holder          │                        │
     │                            │                     │                        │
     │                            │ 19. filterChain     │                        │
     │                            │     .doFilter()     │                        │
     │                            │                     │                        │
     │                            │ Request proceeds    │                        │
     │                            │ with @Secured/      │                        │
     │                            │ @PreAuthorize       │                        │
     │                            │ checks based on     │                        │
     │                            │ authorities         │                        │
     │                            │                     │                        │
```

---

## 3. TENANT CONTEXT INTEGRATION

After successful authentication in `AuthTokenFilter`, you can populate the `TenantContext`:

```
┌────────────────────┐
│ AuthTokenFilter    │
│ (after Auth set)   │
└────────┬───────────┘
         │
         │ After SecurityContextHolder.getContext()
         │         .setAuthentication(auth)
         │
         │ Extract tenantId from principal:
         │ UserDetailsImpl principal = 
         │   (UserDetailsImpl) authentication
         │   .getPrincipal()
         │
         │ TenantContext.setCurrentTenant(
         │   principal.getTenantId()
         │ )
         │
         ├──────────────────────────────────────────>
         │                                    TenantContext
         │                                    (ThreadLocal)
         │
         │ Now all downstream code can access:
         │ UUID tenantId = TenantContext.getCurrentTenant()
         │
```

---

## 4. KEY FILES INVOLVED

| File | Role |
|------|------|
| `AuthController.java` | Sign-in endpoint, generates JWT |
| `JwtUtils.java` | Generates, validates, parses JWT |
| `AuthTokenFilter.java` | Filter that validates JWT on each request |
| `UserDetailsServiceImpl.java` | Loads full UserDetails from DB |
| `UserDetailsImpl.java` | Custom UserDetails with tenantId, location fields |
| `SecurityConfig.java` | Security filter chain configuration |
| `JwtConfig.java` | Provides PrivateKey/PublicKey beans |
| `TenantContext.java` | ThreadLocal storage for current tenantId |

---

## 5. ERROR HANDLING FLOWS

### Sign-in Error Paths

```
POST /signin
    ↓
AuthenticationManager.authenticate()
    ├─ Username not found
    │  └─ UsernameNotFoundException
    │     └─ Caught in AuthController catch(AuthenticationException)
    │        └─ Return HTTP 404 {message: "Bad credentials", status: false}
    │
    ├─ Password mismatch
    │  └─ BadCredentialsException
    │     └─ Caught in AuthController catch(AuthenticationException)
    │        └─ Return HTTP 404 {message: "Bad credentials", status: false}
    │
    └─ Success
       └─ Generate JWT & return LoginResponse
```

### Authenticated Request Error Paths

```
GET /api/notes with Authorization: Bearer token
    ↓
AuthTokenFilter.doFilterInternal()
    ├─ No Authorization header
    │  └─ jwt = null
    │     └─ Skip auth setup
    │        └─ Request continues unauthenticated
    │           └─ @Secured endpoint returns HTTP 401
    │
    ├─ Authorization header but not "Bearer ..."
    │  └─ JWT extraction returns null
    │     └─ Same as above
    │
    ├─ Token is malformed
    │  └─ validateJwtToken catches MalformedJwtException
    │     └─ Returns false, skip auth setup
    │        └─ Request continues unauthenticated
    │           └─ @Secured endpoint returns HTTP 401
    │
    ├─ Token is expired
    │  └─ validateJwtToken catches ExpiredJwtException
    │     └─ Returns false
    │        └─ Request continues unauthenticated
    │           └─ Client must re-login
    │
    ├─ Signature verification fails
    │  └─ validateJwtToken catches SignatureException
    │     └─ Returns false
    │        └─ Request continues unauthenticated
    │           └─ Tampered token rejected
    │
    └─ Token valid
       └─ Load UserDetails and set SecurityContext
          └─ Request authorized based on @PreAuthorize/@Secured
```

---

## 6. SEQUENCE: Happy Path Summary

1. **Client → Server**: `POST /api/auth/public/signin` with `{username, password}`
2. **AuthController** receives and calls `authenticationManager.authenticate()`
3. **Spring Security** uses `UserDetailsServiceImpl` to load `User` from DB → `UserDetailsImpl`
4. **Spring Security** compares passwords (BCrypt) ✓
5. **AuthController** calls `jwtUtils.generateTokenFromUsername(userDetails)` → JWT signed with RSA private key
6. **Server → Client**: `HTTP 200` with `{username, roles, jwtToken}`
7. **Client** stores JWT (in localStorage, cookie, etc.)

---

8. **Client → Server**: `GET /api/notes` with `Authorization: Bearer <jwt>`
9. **AuthTokenFilter** intercepts request
10. **AuthTokenFilter** extracts JWT from header
11. **JwtUtils** validates JWT signature and expiration using RSA public key ✓
12. **JwtUtils** extracts username from token
13. **UserDetailsServiceImpl** loads full `UserDetailsImpl` from DB (including tenantId, roles, location)
14. **AuthTokenFilter** creates `UsernamePasswordAuthenticationToken` with principal and authorities
15. **AuthTokenFilter** sets authentication into `SecurityContextHolder`
16. **(Optional)** `TenantContext.setCurrentTenant(principal.getTenantId())`
17. **FilterChain** continues; controller method executes with authenticated principal
18. **Controller** can access tenant/location via `@AuthenticationPrincipal UserDetailsImpl principal`

---

## 7. TOKEN ANATOMY

```
eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9
.eyJzdWIiOiJhbGljZSIsInJvbGVzIjpbIlJPTEVfVVNFUiJdLCJ0ZW5hbnRJZCI6ImUzMjM4YmQzLWQyMzMtNDcyYSI6IjEwMDAwMDAwMDAwMCIsImlhdCI6MTczNDI4MDU4OCwiZXhwIjoxNzM0NTM5Nzg4fQ
.TJVA95OrM7E2cBab30RMHrHDcEfxjoYZgeFONFh7HgQ
```

| Part | Content | Encoding |
|------|---------|----------|
| **HEADER** | `{"alg":"RS256","typ":"JWT"}` | Base64URL |
| **PAYLOAD** | `{"sub":"alice","roles":["ROLE_USER"],"tenantId":"e3238bd3-d233-472a","exp":1734539788,"iat":1734280588}` | Base64URL |
| **SIGNATURE** | HMAC-SHA256(header.payload, privateKey) | Base64URL |

---

## Notes

- **Private Key** stored securely (env var `JWT_PRIVATE_KEY`), used to **sign** tokens (only server does this)
- **Public Key** used to **verify** tokens (server during filter)
- **TenantContext** is ThreadLocal, so each request thread has its own tenantId
- **Session Management**: Set to `STATELESS` in `SecurityConfig` (no server-side sessions, stateless JWT)
- **CORS**: Configured with frontend URL; credentials allowed

