package com.infyniteloop.isec.security;

import com.infyniteloop.isec.security.jwt.AuthEntryPointJwt;
import com.infyniteloop.isec.security.jwt.AuthTokenFilter;
import com.infyniteloop.isec.security.models.AppRole;
import com.infyniteloop.isec.security.models.Role;
import com.infyniteloop.isec.security.models.User;
import com.infyniteloop.isec.security.repository.RoleRepository;
import com.infyniteloop.isec.security.repository.UserRepository;
import com.infyniteloop.runningroom.booking.entity.Booking;
import com.infyniteloop.runningroom.crew.repository.BookingRepository;
import com.infyniteloop.runningroom.kitchen.entity.Menu;
import com.infyniteloop.runningroom.kitchen.entity.MenuItem;
import com.infyniteloop.runningroom.kitchen.enums.MealCategory;
import com.infyniteloop.runningroom.kitchen.enums.MealType;
import com.infyniteloop.runningroom.kitchen.repository.MenuItemRepository;
import com.infyniteloop.runningroom.kitchen.repository.MenuRepository;
import com.infyniteloop.runningroom.bed.entity.Bed;
import com.infyniteloop.runningroom.building.entity.Building;
import com.infyniteloop.runningroom.room.entity.Room;
import com.infyniteloop.runningroom.model.RunningRoom;
import com.infyniteloop.runningroom.bed.repository.BedRepository;
import com.infyniteloop.runningroom.building.repository.BuildingRepository;
import com.infyniteloop.runningroom.room.repository.RoomRepository;
import com.infyniteloop.runningroom.repository.RunningRoomRepository;
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
                                      UserRepository userRepository, PasswordEncoder passwordEncoder, RoomRepository roomRepository,
                                      BedRepository bedRepository, BuildingRepository buildingRepository, RunningRoomRepository runningRoomRepository,
                                      MenuRepository menuRepository, MenuItemRepository menuItemRepository, BookingRepository bookingRepository) {
        return args -> {
            Role userRole = roleRepository.findByRoleName(AppRole.ROLE_USER)
                    .orElseGet(() -> roleRepository.save(new Role(AppRole.ROLE_USER)));

            Role adminRole = roleRepository.findByRoleName(AppRole.ROLE_ADMIN)
                    .orElseGet(() -> roleRepository.save(new Role(AppRole.ROLE_ADMIN)));

            Role crewRole = roleRepository.findByRoleName(AppRole.ROLE_CREW)
                    .orElseGet(() -> roleRepository.save(new Role(AppRole.ROLE_CREW)));




            RunningRoom tenantNdls = new RunningRoom();
            tenantNdls.setName("NDLS");
            tenantNdls.setDivision("DELHI");
            tenantNdls.setZone("NORTH");

            RunningRoom tenantLko = new RunningRoom();
            tenantLko.setName("LKO");
            tenantLko.setDivision("UP");
            tenantLko.setZone("CENTRAL");

            RunningRoom saverunningRoomN = runningRoomRepository.findByName("NDLS")
                    .orElseGet(() -> runningRoomRepository.save(tenantNdls));
            RunningRoom saverunningRoomL = runningRoomRepository.findByName("LKO")
                    .orElseGet(() -> runningRoomRepository.save(tenantLko));


            Building buildingA = new Building();
            buildingA.setBuildingName("Building NDLS A");
            buildingA.setAddress("123 Main St, City A");
            buildingA.setTenantId(tenantNdls.getId());

            Building buildingB = new Building();
            buildingB.setBuildingName("Building LKO B");
            buildingB.setAddress("456 Elm St, City B");
            buildingB.setTenantId(tenantLko.getId());




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

            bedRepository.findByRoomAndBedNumber(n102,1)
                    .orElseGet(() -> {
                        Bed b = new Bed();
                        b.setBedNumber(1);
                        b.setRoom(n102);
                        b.setTenantId(tenantNdls.getId());
                        return bedRepository.save(b);
                    });
             bedRepository.findByRoomAndBedNumber(n102,2)
                    .orElseGet(() -> {
                        Bed b = new Bed();
                        b.setBedNumber(2);
                        b.setRoom(n102);
                        b.setTenantId(tenantNdls.getId());
                        return bedRepository.save(b);
                    });
            bedRepository.findByRoomAndBedNumber(n102,3)
                    .orElseGet(() -> {
                        Bed b = new Bed();
                        b.setBedNumber(3);
                        b.setRoom(n102);
                        b.setTenantId(tenantNdls.getId());
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

            LocalDate today = LocalDate.now();
            // Calculate the start of the current week (Monday)
            LocalDate startOfCurrentWeek = today.minusDays(today.getDayOfWeek().getValue() - 1);
            LocalDate startOfPreviousWeek = startOfCurrentWeek.minusWeeks(1);

            for (int week = 0; week < 2; week++) {
                LocalDate startDate = week == 0 ? startOfPreviousWeek : startOfCurrentWeek;
                for (int i = 0; i < 7; i++) {
                    LocalDate menuDate = startDate.plusDays(i);
                    if (menuRepository.findByMenuDate(menuDate).isEmpty()) {
                        Menu menu = new Menu();
                        menu.setMenuDate(menuDate);
                        menu.setTenantId(tenantNdls.getId());

                        MenuItem item1 = new MenuItem();
                        item1.setName("Aloo Puri");
                        item1.setDescription("Aloo Puri with Chutney");
                        item1.setPrice(50.0);
                        item1.setMealType(MealType.BREAKFAST);
                        item1.setMealCategory(MealCategory.VEG);
                        item1.setMenu(menu);

                        MenuItem item2 = new MenuItem();
                        item2.setName("Butter Chicken");
                        item2.setDescription("Shahi Paneer with Naan");
                        item2.setPrice(100.0);
                        item2.setMealType(MealType.DINNER);
                        item2.setMealCategory(MealCategory.NON_VEG);
                        item2.setMenu(menu);

                        MenuItem item3 = new MenuItem();
                        item3.setName("Jeera Rice");
                        item3.setDescription("Jeera Rice with Dal");
                        item3.setPrice(80.0);
                        item3.setMealType(MealType.LUNCH);
                        item3.setMealCategory(MealCategory.VEG);
                        item3.setMenu(menu);

                        MenuItem item4 = new MenuItem();
                        item4.setName("Salad");
                        item4.setDescription("Fresh Vegetable Salad");
                        item4.setPrice(30.0);
                        item4.setMealType(MealType.SNACKS);
                        item4.setMealCategory(MealCategory.VEG);

                        item4.setMenu(menu);

                        MenuItem item5 = new MenuItem();
                        item5.setName("Dosa");
                        item5.setDescription("Masala Dosa");
                        item5.setPrice(20.0);
                        item5.setMealType(MealType.BREAKFAST);
                        item5.setMealCategory(MealCategory.VEGAN);
                        item5.setMenu(menu);
                        menu.setItems(List.of(item1, item2, item3, item4, item5));

                        menuRepository.save(menu);
                    }
                }
            }

            List<Bed> beds = bedRepository.findAll();
            for (Bed bed : beds) {
                Booking booking = Booking.builder()
                        .bed(bed)
                        .build();
                booking.setTenantId(bed.getTenantId());

                // Set other fields as needed
                bookingRepository.save(booking);
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
