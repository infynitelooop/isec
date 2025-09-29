package com.infyniteloop.runningroom.controller;

import com.infyniteloop.runningroom.exception.NotFoundException;
import com.infyniteloop.runningroom.model.enums.AttachmentType;
import com.infyniteloop.runningroom.model.enums.CrewType;
import com.infyniteloop.runningroom.kitchen.enums.MealCategory;
import com.infyniteloop.runningroom.kitchen.enums.MealType;
import com.infyniteloop.runningroom.model.enums.OccupancyStatus;
import com.infyniteloop.runningroom.model.enums.RoomCategory;
import com.infyniteloop.runningroom.model.enums.RoomStatus;
import com.infyniteloop.runningroom.model.enums.RoomType;
import com.infyniteloop.runningroom.model.mapper.EnumMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


/**
 * Controller to fetch enum values for various types.
 * This helps in providing enum options to the frontend so that frontend is always in sync with backend.
 * Supported types: attachmentType, crewType, occupancyStatus, roomCategory, roomStatus, roomType
 * Example: /api/enums?type=roomType
 *
 */


@RestController
@RequestMapping("/api/enums")
public class EnumController {

    private final EnumMapper enumMapper;

    public EnumController(EnumMapper enumMapper) {
        this.enumMapper = enumMapper;
    }

    /**
     * Endpoint to get enum values based on the provided type.
     *
     * @param type The type of enum to fetch (e.g., attachmentType, crewType, occupancyStatus, roomCategory, roomStatus, roomType).
     * @return An array of EnumResponse containing key-label pairs for the specified enum type.
     * @throws NotFoundException if the provided type is not recognized.
     */
    @GetMapping
    public EnumMapper.EnumResponse[] getEnumValues(@RequestParam String type) {
        return switch (type.toLowerCase()) {
            case "attachmenttype" -> enumMapper.mapEnum(AttachmentType.values());
            case "crewtype" -> enumMapper.mapEnum(CrewType.values());
            case "occupancystatus" -> enumMapper.mapEnum(OccupancyStatus.values());
            case "roomcategory" -> enumMapper.mapEnum(RoomCategory.values());
            case "roomstatus" -> enumMapper.mapEnum(RoomStatus.values());
            case "roomtype" -> enumMapper.mapEnum(RoomType.values());

            // Kitchen
            case "mealType" -> enumMapper.mapEnum(MealType.values());
            case "mealCategory" -> enumMapper.mapEnum(MealCategory.values());
            default -> throw new NotFoundException("Unknown enum type: " + type);
        };
    }


    /**
     * Endpoint to get all enum values for supported types.
     *
     * @return A map where the key is the enum type and the value is an array of EnumResponse containing key-label pairs.
     */
    @GetMapping("/all")
    public Map<String, EnumMapper.EnumResponse[]> getAllEnums() {
        Map<String, EnumMapper.EnumResponse[]> result = new HashMap<>();
        result.put("roomTypes", enumMapper.mapEnum(RoomType.values()));
        result.put("crewTypes", enumMapper.mapEnum(CrewType.values()));
        result.put("roomStatus", enumMapper.mapEnum(RoomStatus.values()));
        result.put("roomCategory", enumMapper.mapEnum(RoomCategory.values()));
        result.put("attachmentType", enumMapper.mapEnum(AttachmentType.values()));

        // Kitchen
        result.put("mealType", enumMapper.mapEnum(MealType.values()));
        result.put("mealCategory", enumMapper.mapEnum(MealCategory.values()));
        return result;
    }
}
