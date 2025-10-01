package com.infyniteloop.runningroom.booking.dto;

import java.time.LocalDateTime;

public record BedOccupancy(
        String bedNumber,
        String occupancyStatus,
        String crewId,
        String crewName,
        String crewDesignation,
        String crewType,
        String mealType,
        String vegNonVeg,
        LocalDateTime checkInTime,
        Integer restHours,
        LocalDateTime wakeUpTime
) {
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String bedNumber;
        String occupancyStatus;
        private String crewId;
        private String crewName;
        private String crewDesignation;
        private String crewType;
        private String mealType;
        private String vegNonVeg;
        private LocalDateTime checkInTime;
        private Integer restHours;
        private LocalDateTime wakeUpTime;

        public Builder bedNumber(String bedNumber) {
            this.bedNumber = bedNumber;
            return this;
        }

        public Builder occupancyStatus(String occupancyStatus) {
            this.occupancyStatus = occupancyStatus;
            return this;
        }

        public Builder crewId(String crewId) {
            this.crewId = crewId;
            return this;
        }

        public Builder crewName(String crewName) {
            this.crewName = crewName;
            return this;
        }

        public Builder crewDesignation(String crewDesignation) {
            this.crewDesignation = crewDesignation;
            return this;
        }

        public Builder crewType(String crewType) {
            this.crewType = crewType;
            return this;
        }

        public Builder mealType(String mealType) {
            this.mealType = mealType;
            return this;
        }

        public Builder vegNonVeg(String vegNonVeg) {
            this.vegNonVeg = vegNonVeg;
            return this;
        }

        public Builder checkInTime(LocalDateTime checkInTime) {
            this.checkInTime = checkInTime;
            return this;
        }

        public Builder restHours(Integer restHours) {
            this.restHours = restHours;
            return this;
        }

        public Builder wakeUpTime(LocalDateTime wakeUpTime) {
            this.wakeUpTime = wakeUpTime;
            return this;
        }

        public BedOccupancy build() {
            return new BedOccupancy(
                    bedNumber,
                    occupancyStatus,
                    crewId,
                    crewName,
                    crewDesignation,
                    crewType,
                    mealType,
                    vegNonVeg,
                    checkInTime,
                    restHours,
                    wakeUpTime
            );
        }
    }
}