package com.infyniteloop.runningroom.model.types;

/**
 * Enum representing the status of a room.
 */
public enum RoomStatus {
    AVAILABLE,
    OCCUPIED,
    MAINTENANCE,
    CLEANING,
    BLOCKED

    // ⚠️ IMPORTANT:
    // If you add/remove/change any values in this enum,
    // you MUST also update the frontend constants in `enums.js` to keep the UI consistent.
}