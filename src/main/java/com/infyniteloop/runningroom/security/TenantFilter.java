package com.infyniteloop.runningroom.security;

import com.infyniteloop.runningroom.exception.NotFoundException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
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

@Component
public class TenantFilter extends OncePerRequestFilter {

    public static final ThreadLocal<UUID> CURRENT_TENANT = new ThreadLocal<>();

    private final PublicKey publicKey;

    public TenantFilter(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

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
            }

            filterChain.doFilter(request, response);

        } finally {
            // Always clear to avoid leaking to the next request
            CURRENT_TENANT.remove();
        }
    }

    private UUID extractTenantIdFromJWT(String token) {
        try {
            Claims claims = Jwts.parser().verifyWith(publicKey)
                    .build().parseSignedClaims(token).getPayload();

            String tenantIdStr = claims.get("tenantId", String.class);
            if (tenantIdStr == null || tenantIdStr.isBlank()) {
                throw new IllegalStateException("No tenant context found");
            }
            return UUID.fromString(tenantIdStr);

        } catch (Exception e) {
            throw new IllegalArgumentException("No tenant context found", e);
        }
    }



}
