package com.infyniteloop.runningroom.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.hibernate.Session;
import org.springframework.orm.jpa.EntityManagerHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.PublicKey;
import java.util.UUID;


/**
 * TenantFilter is a servlet filter that intercepts incoming HTTP requests to
 * extract and validate JWT tokens from the Authorization header. It retrieves
 * the tenantId claim from the token and sets up a Hibernate filter for
 * multi-tenancy based on this tenantId. The tenantId is also stored in a
 * ThreadLocal variable for easy access throughout the request lifecycle.
 */
@Component
public class TenantFilter extends OncePerRequestFilter {

    public static final ThreadLocal<UUID> CURRENT_TENANT = new ThreadLocal<>();

    private final PublicKey publicKey;
    private final EntityManagerFactory entityManagerFactory;


    public TenantFilter(PublicKey publicKey, EntityManagerFactory entityManagerFactory) {
        this.publicKey = publicKey;
        this.entityManagerFactory = entityManagerFactory;
    }


    /**
     * This filter extracts the JWT token from the Authorization header,
     * validates it, extracts the tenantId claim, and sets up a Hibernate
     * filter for multi-tenancy.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        try {
            String authHeader = request.getHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                UUID tenantId = extractTenantIdFromJWT(token);

                // store in ThreadLocal
                CURRENT_TENANT.set(tenantId);


                // Enable Hibernate filter in the session for multi-tenancy here so that it is called before any repository is used.

                // In Spring/JPA the actual Session/EntityManager that will be used by repositories is created/bound later
                // (usually when a transaction is started or when an EntityManager is bound to the request by OpenEntityManagerInViewFilter).
                // So we cannot just unwrap the EntityManager injected here and enable the filter on it.
                // We need to enable the filter on the actual Session that will be used by the repositories.
                // We need to do it before the repositories are called, so we do it here in the filter.


                // 1) try to get an EntityManager already bound to this thread (if any)
                EntityManagerHolder emHolder =
                        (EntityManagerHolder) TransactionSynchronizationManager.getResource(entityManagerFactory);

                if (emHolder == null) {
                    // No EntityManager bound; create one and bind it for the duration of this request.

                    // Create an EntityManager.
                    // Wrap it in an EntityManagerHolder (Spring’s wrapper).
                    // Register it with TransactionSynchronizationManager under the key entityManagerFactory.
                    // After this, whenever your repositories (JpaRepository, etc.) run inside the same thread, they will pick up that same EntityManager instead of creating a new one.
                    EntityManager em = entityManagerFactory.createEntityManager();
                    emHolder = new EntityManagerHolder(em);
                    TransactionSynchronizationManager.bindResource(entityManagerFactory, emHolder);
                }

                // 2) unwrap session from the bound EntityManager and enable filter
                EntityManager emBound = emHolder.getEntityManager();
                Session session = emBound.unwrap(Session.class);
                session.enableFilter("tenantFilter").setParameter("tenantId", tenantId);
            }

            filterChain.doFilter(request, response);

        } finally {
            CURRENT_TENANT.remove(); // prevent memory leaks . The value is not lost during the request; it is removed after the request finishes.
        }
    }

    /**
     * Extracts the tenantId claim from the JWT token.
     *
     * @param token the JWT token
     * @return the tenantId as a UUID
     * @throws IllegalArgumentException if the token is invalid or tenantId is missing
     */
    private UUID extractTenantIdFromJWT(String token) {
        try {
            Claims claims = Jwts.parser().verifyWith(publicKey)
                    .build().parseSignedClaims(token).getPayload();

            String tenantIdStr = claims.get("tenantId", String.class);
            if (tenantIdStr.isBlank()) {
                throw new IllegalStateException("No tenant context found");
            }
            return UUID.fromString(tenantIdStr);

        } catch (Exception e) {
            throw new IllegalArgumentException("No tenant context found", e);
        }
    }
}
