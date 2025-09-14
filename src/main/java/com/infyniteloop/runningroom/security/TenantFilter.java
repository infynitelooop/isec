package com.infyniteloop.runningroom.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.persistence.EntityManager;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.hibernate.Session;
import org.springframework.stereotype.Component;
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
    private final EntityManager entityManager;


    public TenantFilter(PublicKey publicKey, EntityManager entityManager) {
        this.entityManager = entityManager;
        this.publicKey = publicKey;
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

                // enable Hibernate filter automatically for this request
                Session session = entityManager.unwrap(Session.class);
                session.enableFilter("tenantFilter")
                        .setParameter("tenantId", tenantId);
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
