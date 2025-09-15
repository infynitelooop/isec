package com.infyniteloop.isec.security.jwt;

import com.infyniteloop.isec.security.models.User;
import com.infyniteloop.isec.security.repository.UserRepository;
import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;
import java.util.List;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${spring.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    private final PrivateKey privateKey;
    private final PublicKey publicKey;
    private final UserRepository userRepository;

    // Spring injects the PublicKey bean here from JwtConfig
    public JwtUtils(PrivateKey privateKey, PublicKey publicKey, UserRepository userRepository) {
        this.publicKey = publicKey;
        this.privateKey = privateKey;
        this.userRepository = userRepository;
    }


    public String getJwtFromHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        logger.debug("Authorization Header: {}", bearerToken);
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // Remove Bearer prefix
        }
        return null;
    }

    public String generateTokenFromUsername(UserDetails userDetails) {
        String username = userDetails.getUsername();

        // Load the full user from DB to get tenantId and roles
        User user = userRepository.findByUserName(userDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        List<String> roleNames = user.getRoles()
                .stream()
                .map(role -> role.getRoleName().name()) // AppRole enum name
                .toList();

        return Jwts.builder()
                .subject(username)
                .claim("roles", roleNames)
                .claim("tenantId", user.getTenantId().toString()) // U
                .issuedAt(new Date())
                .expiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(privateKey)
                .compact();
    }

    public String getUserNameFromJwtToken(String token) {
        return getJwtClaims(token)
                .getPayload().getSubject();
    }



    public boolean validateJwtToken(String authToken) {
        try {
            getJwtClaims(authToken);
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }

    private Jws<Claims> getJwtClaims(String authToken) {
        return Jwts.parser().verifyWith(publicKey)
                .build().parseSignedClaims(authToken);
    }
}
