package com.infyniteloop.isec.security;

import com.infyniteloop.isec.security.jwt.AuthEntryPointJwt;
import com.infyniteloop.isec.security.jwt.AuthTokenFilter;
import com.infyniteloop.isec.security.models.AppRole;
import com.infyniteloop.isec.security.models.Role;
import com.infyniteloop.isec.security.models.User;
import com.infyniteloop.isec.security.repository.RoleRepository;
import com.infyniteloop.isec.security.repository.UserRepository;
import com.infyniteloop.runningroom.model.Bed;
import com.infyniteloop.runningroom.model.Building;
import com.infyniteloop.runningroom.model.Room;
import com.infyniteloop.runningroom.model.RunningRoom;
import com.infyniteloop.runningroom.model.Tenant;
import com.infyniteloop.runningroom.repository.BedRepository;
import com.infyniteloop.runningroom.repository.BuildingRepository;
import com.infyniteloop.runningroom.repository.RoomRepository;
import com.infyniteloop.runningroom.repository.RunningRoomRepository;
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
import java.util.Set;
import java.util.UUID;

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
                .requestMatchers("/api/rooms/**").hasRole("ADMIN")
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
                                      RoomRepository roomRepository, BedRepository bedRepository, BuildingRepository buildingRepository, RunningRoomRepository runningRoomRepository) {
        return args -> {
            Role userRole = roleRepository.findByRoleName(AppRole.ROLE_USER)
                    .orElseGet(() -> roleRepository.save(new Role(AppRole.ROLE_USER)));

            Role adminRole = roleRepository.findByRoleName(AppRole.ROLE_ADMIN)
                    .orElseGet(() -> roleRepository.save(new Role(AppRole.ROLE_ADMIN)));

            Role crewRole = roleRepository.findByRoleName(AppRole.ROLE_CREW)
                    .orElseGet(() -> roleRepository.save(new Role(AppRole.ROLE_CREW)));


            Tenant tenantNdls = tenantRepository.findByTenantName("NDLS")
                    .orElseGet(() -> tenantRepository.save(new Tenant("NDLS")));

            Tenant tenantLko = tenantRepository.findByTenantName("LKO")
                    .orElseGet(() -> tenantRepository.save(new Tenant("LKO")));



            RunningRoom runningRoomN = new RunningRoom();
            runningRoomN.setName("Running Room A");
            runningRoomN.setTenantId(tenantNdls.getId());

            RunningRoom runningRoomL = new RunningRoom();
            runningRoomL.setName("Running Room B");
            runningRoomL.setTenantId(tenantLko.getId());

            RunningRoom saverunningRoomN = runningRoomRepository.findByName("Running Room A")
                    .orElseGet(() -> runningRoomRepository.save(runningRoomN));
            RunningRoom saverunningRoomL = runningRoomRepository.findByName("Running Room B")
                    .orElseGet(() -> runningRoomRepository.save(runningRoomL));


            Building buildingA = new Building();
            buildingA.setBuildingName("Building NDLS A");
            buildingA.setAddress("123 Main St, City A");
            buildingA.setTenantId(tenantNdls.getId());
            buildingA.setRunningRoom(saverunningRoomN);

            Building buildingB = new Building();
            buildingB.setBuildingName("Building LKO B");
            buildingB.setAddress("456 Elm St, City B");
            buildingB.setTenantId(tenantLko.getId());
            buildingB.setRunningRoom(saverunningRoomL);




            Building saveBuildingA = buildingRepository.findByBuildingName("Building NDLS A")
                    .orElseGet(() -> buildingRepository.save(buildingA));
            Building saveBuildingB = buildingRepository.findByBuildingName("Building LKO B")
                    .orElseGet(() -> buildingRepository.save(buildingB));


            Room n101 = roomRepository.findByRoomNumber("N101")
                    .orElseGet(() -> roomRepository.save(build("N101", tenantNdls.getId(), saveBuildingA)));
            Room n102 = roomRepository.findByRoomNumber("N102")
                    .orElseGet(() -> roomRepository.save(build("N102", tenantNdls.getId(), saveBuildingA)));
            Room l103 = roomRepository.findByRoomNumber("L103")
                    .orElseGet(() -> roomRepository.save(build("L103", tenantLko.getId(), saveBuildingB)));
            Room l104 = roomRepository.findByRoomNumber("L104")
                    .orElseGet(() -> roomRepository.save(build("L104", tenantLko.getId(), saveBuildingB)));


            Bed bed1 = bedRepository.findByBedNumber("1")
                    .orElseGet(() -> {
                        Bed b = new Bed();
                        b.setBedNumber("1");
                        b.setRoom(n101);
                        b.setTenantId(tenantNdls.getId());
                        return bedRepository.save(b);
                    });
            Bed bed2 = bedRepository.findByBedNumber("2")
                    .orElseGet(() -> {
                        Bed b = new Bed();
                        b.setBedNumber("2");
                        b.setRoom(n101);
                        b.setTenantId(tenantNdls.getId());
                        return bedRepository.save(b);
                    });
            Bed bed3 = bedRepository.findByBedNumber("3")
                    .orElseGet(() -> {
                        Bed b = new Bed();
                        b.setBedNumber("3");
                        b.setRoom(l103);
                        b.setTenantId(tenantLko.getId());
                        return bedRepository.save(b);
                    });
            Bed bed4 = bedRepository.findByBedNumber("4")
                    .orElseGet(() -> {
                        Bed b = new Bed();
                        b.setBedNumber("4");
                        b.setRoom(l103);
                        b.setTenantId(tenantLko.getId());
                        return bedRepository.save(b);
                    });



            if (!userRepository.existsByUserName("user1")) {
                User user1 = new User("user1", "user1@example.com", passwordEncoder.encode("password1"),
                        "Rajiv","Pandey","9876543210");
                user1.setAccountNonLocked(false);
                user1.setAccountNonExpired(true);
                user1.setCredentialsNonExpired(true);
                user1.setEnabled(true);
                user1.setCredentialsExpiryDate(LocalDate.now().plusYears(1));
                user1.setAccountExpiryDate(LocalDate.now().plusYears(1));
                user1.setTwoFactorEnabled(false);
                user1.setSignUpMethod("email");
                user1.setRoles(Set.of(userRole));
                user1.setTenantId(tenantNdls.getId());
                userRepository.save(user1);
            }

            if (!userRepository.existsByUserName("admin")) {
                User admin = new User("admin", "admin@example.com", passwordEncoder.encode("adminPass"),
                        "Super","Admin","9876543211");
                admin.setAccountNonLocked(true);
                admin.setAccountNonExpired(true);
                admin.setCredentialsNonExpired(true);
                admin.setEnabled(true);
                admin.setCredentialsExpiryDate(LocalDate.now().plusYears(1));
                admin.setAccountExpiryDate(LocalDate.now().plusYears(1));
                admin.setTwoFactorEnabled(false);
                admin.setSignUpMethod("email");
                admin.setRoles(Set.of(userRole, adminRole));
                admin.setTenantId(tenantNdls.getId());
                userRepository.save(admin);
            }

            if (!userRepository.existsByUserName("rangasanju")) {
                User admin = new User("rangasanju", "sanjay.ranga.au@gmail.com", passwordEncoder.encode("adminPass"),
                        "Sanjay","Ranga","9876543212");
                admin.setAccountNonLocked(true);
                admin.setAccountNonExpired(true);
                admin.setCredentialsNonExpired(true);
                admin.setEnabled(true);
                admin.setCredentialsExpiryDate(LocalDate.now().plusYears(1));
                admin.setAccountExpiryDate(LocalDate.now().plusYears(1));
                admin.setTwoFactorEnabled(false);
                admin.setSignUpMethod("email");
                admin.setRoles(Set.of(userRole, adminRole));
                admin.setTenantId(tenantLko.getId());
                userRepository.save(admin);
            }

            if (!userRepository.existsByUserName("RTM1001")) {
                User admin = new User("RTM1001", "rtm1001@gmail.com", passwordEncoder.encode("password1"),
                        "John","Wick","9876543212");
                admin.setAccountNonLocked(true);
                admin.setAccountNonExpired(true);
                admin.setCredentialsNonExpired(true);
                admin.setEnabled(true);
                admin.setCredentialsExpiryDate(LocalDate.now().plusYears(1));
                admin.setAccountExpiryDate(LocalDate.now().plusYears(1));
                admin.setTwoFactorEnabled(false);
                admin.setSignUpMethod("email");
                admin.setRoles(Set.of(userRole, crewRole));
                admin.setTenantId(null);
                userRepository.save(admin);
            }
        };
    }


    private Room build(String roomNumber, UUID tenantId, Building building) {
        Room room = new Room();
        room.setRoomNumber(roomNumber);
        room.setTenantId(tenantId);
        room.setBuilding(building);
        return room;
    }

}
