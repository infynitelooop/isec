package com.infyniteloop.runningroom.enums.enums;

/**
 * Enum representing the type of a room.
 */
// TODO: Consider the facility to add Type by the customer and expose endpoints
public enum RoomType {
    SINGLE,
    DOUBLE,
    SUITE

    // ⚠️ IMPORTANT:
    // If you add/remove/change any values in this enum,
    // you MUST also update the frontend constants in `enums.js` to keep the UI consistent.
}