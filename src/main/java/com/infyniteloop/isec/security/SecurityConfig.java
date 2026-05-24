package com.infyniteloop.isec.security;

import com.infyniteloop.isec.security.jwt.AuthEntryPointJwt;
import com.infyniteloop.isec.security.jwt.AuthTokenFilter;
import com.infyniteloop.runningroom.building.entity.Building;
import com.infyniteloop.runningroom.room.entity.Room;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class SecurityConfig {

    private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);

    @Value("${app.frontend.url}")
    String frontendUrl;

    private final AuthEntryPointJwt unauthorizedHandler;

    public SecurityConfig(AuthEntryPointJwt unauthorizedHandler) {
        this.unauthorizedHandler = unauthorizedHandler;
    }

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {

        // Disable CSRF for /api/** endpoints and enable stateless session management (as we are using JWT)
        http.csrf(csrf ->
                        csrf.ignoringRequestMatchers("/api/**"))
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS));


        // Enable CORS with custom configuration
        http.cors(
                cors -> cors.configurationSource(corsConfigurationSource())
        );
        http.authorizeHttpRequests(requests -> requests

                .requestMatchers("/api/auth/public/**").permitAll()
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .requestMatchers("/api/rooms/**").hasRole("ADMIN")
                .requestMatchers("/api/crew/**").hasRole("ADMIN")
                .requestMatchers("/api/notes/**").hasAnyRole("USER","ADMIN")
                .requestMatchers("/api/enums/**").hasAnyRole("USER","ADMIN")
//                .requestMatchers("/api/audit/**").hasRole("ADMIN")
                .requestMatchers("/api/user/**").hasRole("USER")
                .requestMatchers("/api/auth/user/**").hasAnyRole("USER","ADMIN")
                .requestMatchers("/api/csrf-token").permitAll()
                .requestMatchers("/api/public/**").permitAll()
                .requestMatchers("/h2-console/**").permitAll()
                .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                .requestMatchers("/actuator/**").permitAll()
                .anyRequest().authenticated());

        // The default exception handling mechanism is unauthorizedHandler
        http.exceptionHandling(exception
                -> exception.authenticationEntryPoint(unauthorizedHandler));

        // Add the JWT token filter before the UsernamePasswordAuthenticationFilter
        http.addFilterBefore(authenticationJwtTokenFilter(),
                UsernamePasswordAuthenticationFilter.class);
        //http.csrf(AbstractHttpConfigurer::disable);
        http.httpBasic(withDefaults());
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }


    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }



    // GLOBAL settings for CORS
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        // Allow specific origins

        log.info(">>frontendUrls={}", frontendUrl);
        corsConfig.setAllowedOrigins(List.of(frontendUrl));

        // Allow specific HTTP methods
        corsConfig.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        // Allow specific headers
        corsConfig.setAllowedHeaders(List.of("*"));
        // Allow credentials (cookies, authorization headers)
        corsConfig.setAllowCredentials(true);
        corsConfig.setMaxAge(3600L);
        // Define allowed paths (for all paths use "/**")
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig); // Apply to all endpoints
        return source;
    }

    private Room build(String roomNumber, UUID tenantId, Building building) {
        Room room = new Room();
        room.setRoomNumber(roomNumber);
        room.setTenantId(tenantId);
        room.setBuilding(building);
        return room;
    }

}
