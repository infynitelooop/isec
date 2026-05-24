package com.infyniteloop.isec.security.jwt;

import com.infyniteloop.isec.security.services.UserDetailsServiceImpl;
import com.infyniteloop.isec.security.services.impl.UserDetailsImpl;
import com.infyniteloop.runningroom.util.TenantContext;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
public class AuthTokenFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        logger.debug("AuthTokenFilter called for URI: {}", request.getRequestURI());
        try {
            String jwt = parseJwt(request);
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                String username = jwtUtils.getUserNameFromJwtToken(jwt);

                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails,
                                null,
                                userDetails.getAuthorities());
                logger.debug("Roles from JWT: {}", userDetails.getAuthorities());

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);

                // Set tenantId in ThreadLocal context for downstream access
                if (userDetails instanceof UserDetailsImpl) {
                    UserDetailsImpl userDetailsImpl = (UserDetailsImpl) userDetails;
                    UUID tenantId = userDetailsImpl.getTenantId();
                    if (tenantId != null) {
                        TenantContext.setCurrentTenant(tenantId);
                        logger.debug("TenantContext set for tenant: {}", tenantId);
                    } else {
                        logger.warn("TenantId is null for user: {}", username);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Cannot set user authentication: {}", e);
        } finally {
            // Clean up TenantContext to prevent ThreadLocal leaks
            // This is called after the request is fully processed
            try {
                filterChain.doFilter(request, response);
            } finally {
                TenantContext.clear();
            }
        }
    }

    private String parseJwt(HttpServletRequest request) {
        String jwt = jwtUtils.getJwtFromHeader(request);
        logger.debug("AuthTokenFilter.java: {}", jwt);
        return jwt;
    }
}
