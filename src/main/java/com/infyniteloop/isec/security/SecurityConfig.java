package com.infyniteloop.isec.security;

import com.infyniteloop.isec.security.jwt.AuthEntryPointJwt;
import com.infyniteloop.isec.security.jwt.AuthTokenFilter;
import com.infyniteloop.isec.security.models.AppRole;
import com.infyniteloop.isec.security.models.Role;
import com.infyniteloop.isec.security.models.User;
import com.infyniteloop.isec.security.repository.RoleRepository;
import com.infyniteloop.isec.security.repository.UserRepository;
import com.infyniteloop.runningroom.model.Room;
import com.infyniteloop.runningroom.model.Tenant;
import com.infyniteloop.runningroom.repository.RoomRepository;
import com.infyniteloop.runningroom.repository.TenantRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
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

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class SecurityConfig {

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
                .requestMatchers("/api/notes/**").hasAnyRole("USER","ADMIN")
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





    @Bean
    public CommandLineRunner initData(RoleRepository roleRepository,
                                      UserRepository userRepository, PasswordEncoder passwordEncoder, TenantRepository tenantRepository,
                                      RoomRepository roomRepository) {
        return args -> {
            Role userRole = roleRepository.findByRoleName(AppRole.ROLE_USER)
                    .orElseGet(() -> roleRepository.save(new Role(AppRole.ROLE_USER)));

            Role adminRole = roleRepository.findByRoleName(AppRole.ROLE_ADMIN)
                    .orElseGet(() -> roleRepository.save(new Role(AppRole.ROLE_ADMIN)));


            Tenant tenant_Ndls = tenantRepository.findByTenantName("NDLS")
                    .orElseGet(() -> tenantRepository.save(new Tenant("NDLS")));

            Tenant tenant_Lko = tenantRepository.findByTenantName("LKO")
                    .orElseGet(() -> tenantRepository.save(new Tenant("LKO")));


            roomRepository.findByRoomNumber("101")
                    .orElseGet(() -> roomRepository.save(new Room("101", tenant_Ndls.getId())));
            roomRepository.findByRoomNumber("102")
                    .orElseGet(() -> roomRepository.save(new Room("102", tenant_Ndls.getId())));



            if (!userRepository.existsByUserName("user1")) {
                User user1 = new User("user1", "user1@example.com", passwordEncoder.encode("password1"));
                user1.setAccountNonLocked(false);
                user1.setAccountNonExpired(true);
                user1.setCredentialsNonExpired(true);
                user1.setEnabled(true);
                user1.setCredentialsExpiryDate(LocalDate.now().plusYears(1));
                user1.setAccountExpiryDate(LocalDate.now().plusYears(1));
                user1.setTwoFactorEnabled(false);
                user1.setSignUpMethod("email");
                user1.setRole(userRole);
                user1.setTenantId(tenant_Ndls.getId());
                userRepository.save(user1);
            }

            if (!userRepository.existsByUserName("admin")) {
                User admin = new User("admin", "admin@example.com", passwordEncoder.encode("adminPass"));
                admin.setAccountNonLocked(true);
                admin.setAccountNonExpired(true);
                admin.setCredentialsNonExpired(true);
                admin.setEnabled(true);
                admin.setCredentialsExpiryDate(LocalDate.now().plusYears(1));
                admin.setAccountExpiryDate(LocalDate.now().plusYears(1));
                admin.setTwoFactorEnabled(false);
                admin.setSignUpMethod("email");
                admin.setRole(adminRole);
                admin.setTenantId(tenant_Ndls.getId());
                userRepository.save(admin);
            }

            if (!userRepository.existsByUserName("rangasanju")) {
                User admin = new User("rangasanju", "sanjay.ranga.au@gmail.com", passwordEncoder.encode("adminPass"));
                admin.setAccountNonLocked(true);
                admin.setAccountNonExpired(true);
                admin.setCredentialsNonExpired(true);
                admin.setEnabled(true);
                admin.setCredentialsExpiryDate(LocalDate.now().plusYears(1));
                admin.setAccountExpiryDate(LocalDate.now().plusYears(1));
                admin.setTwoFactorEnabled(false);
                admin.setSignUpMethod("email");
                admin.setRole(adminRole);
                admin.setTenantId(tenant_Ndls.getId());
                userRepository.save(admin);
            }
        };
    }




}
