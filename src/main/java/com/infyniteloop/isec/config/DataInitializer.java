package com.infyniteloop.isec.config;

import com.infyniteloop.isec.security.models.AppRole;
import com.infyniteloop.isec.security.models.Role;
import com.infyniteloop.isec.security.models.User;
import com.infyniteloop.isec.security.repository.RoleRepository;
import com.infyniteloop.isec.security.repository.UserRepository;
import com.infyniteloop.runningroom.bed.entity.Bed;
import com.infyniteloop.runningroom.bed.repository.BedRepository;
import com.infyniteloop.runningroom.booking.entity.Booking;
import com.infyniteloop.runningroom.building.entity.Building;
import com.infyniteloop.runningroom.building.repository.BuildingRepository;
import com.infyniteloop.runningroom.crew.entity.Crew;
import com.infyniteloop.runningroom.booking.repository.BookingRepository;
import com.infyniteloop.runningroom.crew.repository.CrewRepository;
import com.infyniteloop.runningroom.enums.enums.OccupancyStatus;
import com.infyniteloop.runningroom.kitchen.entity.Menu;
import com.infyniteloop.runningroom.kitchen.entity.MenuItem;
import com.infyniteloop.runningroom.kitchen.enums.MealCategory;
import com.infyniteloop.runningroom.kitchen.enums.MealType;
import com.infyniteloop.runningroom.kitchen.repository.MenuRepository;
import com.infyniteloop.runningroom.room.entity.Room;
import com.infyniteloop.runningroom.room.repository.RoomRepository;
import com.infyniteloop.runningroom.runningroom.entity.Division;
import com.infyniteloop.runningroom.runningroom.entity.Zone;
import com.infyniteloop.runningroom.runningroom.entity.RunningRoom;
import com.infyniteloop.runningroom.runningroom.repository.DivisionRepository;
import com.infyniteloop.runningroom.runningroom.repository.ZoneRepository;
import com.infyniteloop.runningroom.runningroom.repository.RunningRoomRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Inserts basic test data at application startup when profile is not 'prod'.
 */
@Component
@Profile("!prod")
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RunningRoomRepository runningRoomRepository;
    private final ZoneRepository zoneRepository;
    private final DivisionRepository divisionRepository;
    private final RoomRepository roomRepository;
    private final BuildingRepository buildingRepository;
    private final BedRepository bedRepository;
    private final MenuRepository menuRepository;
    private final BookingRepository bookingRepository;
    private final CrewRepository crewRepository;

    public DataInitializer(RoleRepository roleRepository,
                           UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           RunningRoomRepository runningRoomRepository,
                           ZoneRepository zoneRepository,
                           DivisionRepository divisionRepository,
                           RoomRepository roomRepository,
                           BuildingRepository buildingRepository,
                           BedRepository bedRepository,
                           MenuRepository menuRepository,
                           BookingRepository bookingRepository,
                           CrewRepository crewRepository
    ) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.runningRoomRepository = runningRoomRepository;
        this.zoneRepository = zoneRepository;
        this.divisionRepository = divisionRepository;
        this.roomRepository = roomRepository;
        this.buildingRepository = buildingRepository;
        this.bedRepository = bedRepository;
        this.menuRepository = menuRepository;
        this.bookingRepository = bookingRepository;
        this.crewRepository = crewRepository;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        log.info("Running DataInitializer...");

        // Roles
        Role userRole = roleRepository.findByRoleName(AppRole.ROLE_USER)
                .orElseGet(() -> roleRepository.save(new Role(AppRole.ROLE_USER)));
        Role adminRole = roleRepository.findByRoleName(AppRole.ROLE_ADMIN)
                .orElseGet(() -> roleRepository.save(new Role(AppRole.ROLE_ADMIN)));
        roleRepository.findByRoleName(AppRole.ROLE_CREW)
                .orElseGet(() -> roleRepository.save(new Role(AppRole.ROLE_CREW)));

        // Zones and Divisions
        Zone northZone = zoneRepository.findByName("NORTH")
                .orElseGet(() -> zoneRepository.save(new Zone(null, "NORTH", null)));
        Zone southZone = zoneRepository.findByName("SOUTH")
                .orElseGet(() -> zoneRepository.save(new Zone(null, "SOUTH", null)));

        Division delhiDivision = divisionRepository.findByName("DELHI")
                .orElseGet(() -> {
                    Division d = new Division();
                    d.setName("DELHI");
                    d.setZone(northZone);
                    return divisionRepository.save(d);
                });

        Division lkoDivision = divisionRepository.findByName("LUCKNOW")
                .orElseGet(() -> {
                    Division d = new Division();
                    d.setName("LUCKNOW");
                    d.setZone(northZone);
                    return divisionRepository.save(d);
                });

        Division chennaiDivision = divisionRepository.findByName("CHENNAI")
                .orElseGet(() -> {
                    Division d = new Division();
                    d.setName("CHENNAI");
                    d.setZone(southZone);
                    return divisionRepository.save(d);
                });

        // Running Rooms
        RunningRoom ndls = runningRoomRepository.findByName("NDLS")
                .orElseGet(() -> {
                    RunningRoom r = new RunningRoom();
                    r.setName("NDLS");
                    r.setDivision(delhiDivision);
                    return runningRoomRepository.save(r);
                });

        RunningRoom lko = runningRoomRepository.findByName("LKO")
                .orElseGet(() -> {
                    RunningRoom r = new RunningRoom();
                    r.setName("LKO");
                    r.setDivision(lkoDivision);
                    return runningRoomRepository.save(r);
                });

        RunningRoom chn = runningRoomRepository.findByName("CHN")
                .orElseGet(() -> {
                    RunningRoom r = new RunningRoom();
                    r.setName("CHN");
                    r.setDivision(chennaiDivision);
                    return runningRoomRepository.save(r);
                });


        // Users
        if (!userRepository.existsByUserName("admin")) {
            User admin = new User("admin", "admin@example.com", passwordEncoder.encode("adminPass"),
                    "Super", "Admin", "9876543211");
            Set<Role> roles = new HashSet<>();
            roles.add(userRole);
            roles.add(adminRole);
            admin.setRoles(roles);
            admin.setTenantId(ndls.getId());
            userRepository.save(admin);
            log.info("Created admin user");
        }

        if (!userRepository.existsByUserName("Rajiv")) {
            User user1 = new User("Rajiv", "Rajiv@example.com", passwordEncoder.encode("password1"),
                    "Rajiv", "Pandey", "9876543210");
            Set<Role> roles = new HashSet<>();
            roles.add(userRole);
            user1.setRoles(roles);
            user1.setTenantId(ndls.getId());
            userRepository.save(user1);
            log.info("Created user Rajiv");
        }

        if (!userRepository.existsByUserName("Rajni")) {
            User user1 = new User("Rajni", "Rajni@example.com", passwordEncoder.encode("password1"),
                    "Rajni", "Kant", "9990016109");
            Set<Role> roles = new HashSet<>();
            roles.add(adminRole);
            roles.add(userRole);
            user1.setRoles(roles);
            user1.setTenantId(chn.getId());
            userRepository.save(user1);
            log.info("Created user Rajni");
        }

        // Initialize Crew data for Ratlam (RTM)
        String[] crewNames = {
                "Rajesh Kumar", "Priya Singh", "Ramesh Verma", "Ankita Rao",
                "Vikram Patel", "Neha Sharma", "Arjun Desai", "Sapna Gupta",
                "Arun Nair", "Divya Reddy"
        };

        String[] fathersNames = {
                "Ram Kumar", "Suresh Singh", "Hari Verma", "Rajesh Rao",
                "Kiran Patel", "Anil Sharma", "Ravi Desai", "Amit Gupta",
                "Krishna Nair", "Venkat Reddy"
        };

        String[] mobileNumbers = {
                "9876543210", "9876543211", "9876543212", "9876543213",
                "9876543214", "9876543215", "9876543216", "9876543217",
                "9876543218", "9876543219"
        };

        for (int i = 1; i <= 10; i++) {
            String crewId = String.format("RTM%04d", i);
            if (!crewRepository.existsById(crewId)) {
                Crew crew = new Crew();
                crew.setCrewId(crewId);
                crew.setName(crewNames[i - 1]);
                crew.setFathersName(fathersNames[i - 1]);
                crew.setGender(i % 2 == 0 ? "Female" : "Male");
                crew.setDateOfBirth(LocalDate.of(1985 + (i % 10), (i % 12) + 1, (i % 28) + 1));
                crew.setMobileNumber(mobileNumbers[i - 1]);
                crew.setAddress("Ratlam, Madhya Pradesh - " + (450000 + i));
                crew.setPermanentAddress("Ratlam, Madhya Pradesh - " + (450000 + i));
                crew.setMaritalStatus(i % 3 == 0 ? "Married" : "Single");
                crew.setBloodGroup(new String[]{"O+", "A+", "B+", "AB+"}[i % 4]);
                crew.setEmergencyContactNumber(mobileNumbers[(i - 1 + 5) % 10]);
                crew.setDesignation(new String[]{"Driver", "Guard", "Attendant", "Engineer", "Operator"}[i % 5]);
                crew.setCrewType("RRTM");
                crew.setOrgType("RLY");
                crew.setHqCode("RTM");
                crew.setCadre("A");
                crewRepository.save(crew);
                log.info("Created crew with ID: {}", crewId);
            }
        }

        Building buildingA = new Building();
        buildingA.setBuildingName("Building NDLS A");
        buildingA.setAddress("123 Main St, City A");
        buildingA.setTenantId(ndls.getId());

        Building buildingNB = new Building();
        buildingNB.setBuildingName("Building NDLS B");
        buildingNB.setAddress("123B Main St, City A");
        buildingNB.setTenantId(ndls.getId());

        Building buildingLA = new Building();
        buildingLA.setBuildingName("Building CHN A");
        buildingLA.setAddress("456 Elm St, City B");
        buildingLA.setTenantId(chn.getId());

        Building saveBuildingA = buildingRepository.findByBuildingName("Building NDLS A")
                .orElseGet(() -> buildingRepository.save(buildingA));
        Building saveBuildingNB = buildingRepository.findByBuildingName("Building NDLS B")
                .orElseGet(() -> buildingRepository.save(buildingNB));
        Building saveBuildingB = buildingRepository.findByBuildingName("Building CHN A")
                .orElseGet(() -> buildingRepository.save(buildingLA));


        Room na101 = roomRepository.findByRoomNumber("NA101")
                .orElseGet(() -> roomRepository.save(build("NA101", ndls.getId(), saveBuildingA)));
        Room na102 = roomRepository.findByRoomNumber("NA102")
                .orElseGet(() -> roomRepository.save(build("NA102", ndls.getId(), saveBuildingA)));
        Room nb102 = roomRepository.findByRoomNumber("NB102")
                .orElseGet(() -> roomRepository.save(build("NB102", ndls.getId(), saveBuildingNB)));
        Room chn101 = roomRepository.findByRoomNumber("C101")
                .orElseGet(() -> roomRepository.save(build("C101", chn.getId(), saveBuildingB)));
        Room chn102 = roomRepository.findByRoomNumber("C102")
                .orElseGet(() -> roomRepository.save(build("C102", chn.getId(), saveBuildingB)));


        Bed bed1_n101 = bedRepository.findByRoomAndBedNumber(na101, 1)
                .orElseGet(() -> {
                    Bed b = new Bed();
                    b.setBedNumber(1);
                    b.setRoom(na101);
                    b.setTenantId(ndls.getId());
                    b.setOccupancyStatus(OccupancyStatus.AVAILABLE);
                    return bedRepository.save(b);
                });

        Bed bed2_n101 = bedRepository.findByRoomAndBedNumber(na101, 2)
                .orElseGet(() -> {
                    Bed b = new Bed();
                    b.setBedNumber(2);
                    b.setRoom(na101);
                    b.setTenantId(ndls.getId());
                    b.setOccupancyStatus(OccupancyStatus.AVAILABLE);
                    return bedRepository.save(b);
                });

        Bed bed1_n102 = bedRepository.findByRoomAndBedNumber(na102, 2)
                .orElseGet(() -> {
                    Bed b = new Bed();
                    b.setBedNumber(1);
                    b.setRoom(na102);
                    b.setTenantId(ndls.getId());
                    b.setOccupancyStatus(OccupancyStatus.AVAILABLE);
                    return bedRepository.save(b);
                });

        Bed bed1_nb102 = bedRepository.findByRoomAndBedNumber(nb102, 1)
                .orElseGet(() -> {
                    Bed b = new Bed();
                    b.setBedNumber(1);
                    b.setRoom(nb102);
                    b.setTenantId(ndls.getId());
                    b.setOccupancyStatus(OccupancyStatus.AVAILABLE);
                    return bedRepository.save(b);
                });




        Bed bed1_chn101 = bedRepository.findByRoomAndBedNumber(chn101, 3)
                .orElseGet(() -> {
                    Bed b = new Bed();
                    b.setBedNumber(1);
                    b.setRoom(chn101);
                    b.setTenantId(chn.getId());
                    b.setOccupancyStatus(OccupancyStatus.AVAILABLE);
                    return bedRepository.save(b);
                });
        Bed bed1_chn102 = bedRepository.findByRoomAndBedNumber(chn102, 4)
                .orElseGet(() -> {
                    Bed b = new Bed();
                    b.setBedNumber(1);
                    b.setRoom(chn102);
                    b.setTenantId(chn.getId());
                    b.setOccupancyStatus(OccupancyStatus.AVAILABLE);
                    return bedRepository.save(b);
                });


        LocalDate today = LocalDate.now();
        // Calculate the start of the current week (Monday)
        LocalDate startOfCurrentWeek = today.minusDays(today.getDayOfWeek().getValue() - 1);
        LocalDate startOfPreviousWeek = startOfCurrentWeek.minusWeeks(1);

        for (int week = 0; week < 2; week++) {
            LocalDate startDate = week == 0 ? startOfPreviousWeek : startOfCurrentWeek;
            for (int i = 0; i < 7; i++) {
                LocalDate menuDate = startDate.plusDays(i);
                
                // Create menus for NDLS if not exists
                if (!menuRepository.existsByMenuDateAndTenantId(menuDate, ndls.getId())) {
                    Menu menu = new Menu();
                    menu.setMenuDate(menuDate);

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
                    menu.setTenantId(ndls.getId());
                    menuRepository.save(menu);
                    log.info("Created menu for NDLS on {}", menuDate);
                }
                
                // Create menus for CHN if not exists
                if (!menuRepository.existsByMenuDateAndTenantId(menuDate, chn.getId())) {
                    Menu menuChn = new Menu();
                    menuChn.setMenuDate(menuDate);
                    
                    MenuItem item1 = new MenuItem();
                    item1.setName("Aloo Puri");
                    item1.setDescription("Aloo Puri with Chutney");
                    item1.setPrice(50.0);
                    item1.setMealType(MealType.BREAKFAST);
                    item1.setMealCategory(MealCategory.VEG);
                    item1.setMenu(menuChn);

                    MenuItem item2 = new MenuItem();
                    item2.setName("Butter Chicken");
                    item2.setDescription("Shahi Paneer with Naan");
                    item2.setPrice(100.0);
                    item2.setMealType(MealType.DINNER);
                    item2.setMealCategory(MealCategory.NON_VEG);
                    item2.setMenu(menuChn);

                    MenuItem item3 = new MenuItem();
                    item3.setName("Jeera Rice");
                    item3.setDescription("Jeera Rice with Dal");
                    item3.setPrice(80.0);
                    item3.setMealType(MealType.LUNCH);
                    item3.setMealCategory(MealCategory.VEG);
                    item3.setMenu(menuChn);

                    MenuItem item4 = new MenuItem();
                    item4.setName("Salad");
                    item4.setDescription("Fresh Vegetable Salad");
                    item4.setPrice(30.0);
                    item4.setMealType(MealType.SNACKS);
                    item4.setMealCategory(MealCategory.VEG);
                    item4.setMenu(menuChn);

                    MenuItem item5 = new MenuItem();
                    item5.setName("Dosa");
                    item5.setDescription("Masala Dosa");
                    item5.setPrice(20.0);
                    item5.setMealType(MealType.BREAKFAST);
                    item5.setMealCategory(MealCategory.VEGAN);
                    item5.setMenu(menuChn);
                    
                    menuChn.setItems(List.of(item1, item2, item3, item4, item5));
                    menuChn.setTenantId(chn.getId());
                    menuRepository.save(menuChn);
                    log.info("Created menu for CHN on {}", menuDate);
                }
            }
        }

        List<Bed> beds = bedRepository.findAll();
        for (Bed bed : beds) {

            bookingRepository.findByBed(bed)
                    .orElseGet(() -> {
                        Booking booking = Booking.builder()
                                .bed(bed)
                                .build();
                        booking.setTenantId(bed.getTenantId());
                        return  bookingRepository.save(booking);
                    });
        }


        log.info("Data initializer completed.");
}

private Room build(String roomNumber, UUID tenantId, Building building) {
    Room room = new Room();
    room.setRoomNumber(roomNumber);
    room.setTenantId(tenantId);
    room.setBuilding(building);
    return room;
}

}

