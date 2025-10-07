package com.infyniteloop.runningroom.booking.dto;

import java.time.LocalDateTime;

public record BookingResponse(
        Long bookingId,
        String occupancyStatus,
        String bedNumber,
        String roomNumber,
        String buildingName,
        String crewId,
        String crewName,
        String crewDesignation,
        String crewType,
        String mealType,
        String vegNonVeg,
        LocalDateTime checkInTime,
        LocalDateTime checkOutTime,
        Boolean subsidizedFood,
        String attachmentPref,
        String signOffStation,
        LocalDateTime signOffDateTime,
        Integer restHours,
        String taSno,
        String ccId,
        String ccUserId,
        LocalDateTime signOffApprovalTime,
        String signOnNoV,
        LocalDateTime transactionTime,
        Integer noOfMeals,
        LocalDateTime wakeUpTime
) {

    // ✅ Builder inner class
    public static class Builder {
        private Long bookingId;
        private String occupancyStatus;
        private String bedNumber;
        private String roomNumber;
        private String buildingName;
        private String crewId;
        private String crewName;
        private String crewDesignation;
        private String crewType;
        private String mealType;
        private String vegNonVeg;
        private LocalDateTime checkInTime;
        private LocalDateTime checkOutTime;
        private Boolean subsidizedFood;
        private String attachmentPref;
        private String signOffStation;
        private LocalDateTime signOffDateTime;
        private Integer restHours;
        private String taSno;
        private String ccId;
        private String ccUserId;
        private LocalDateTime signOffApprovalTime;
        private String signOnNoV;
        private LocalDateTime transactionTime;
        private Integer noOfMeals;
        private LocalDateTime wakeUpTime;

        public Builder bookingId(Long bookingId) {
            this.bookingId = bookingId;
            return this;
        }

        public Builder occupancyStatus(String occupancyStatus) {
            this.occupancyStatus = occupancyStatus;
            return this;
        }

        public Builder bedNumber(String bedNumber) {
            this.bedNumber = bedNumber;
            return this;
        }

        public Builder roomNumber(String roomNumber) {
            this.roomNumber = roomNumber;
            return this;
        }

        public Builder buildingName(String buildingName) {
            this.buildingName = buildingName;
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

        public Builder checkOutTime(LocalDateTime checkOutTime) {
            this.checkOutTime = checkOutTime;
            return this;
        }

        public Builder subsidizedFood(Boolean subsidizedFood) {
            this.subsidizedFood = subsidizedFood;
            return this;
        }

        public Builder attachmentPref(String attachmentPref) {
            this.attachmentPref = attachmentPref;
            return this;
        }

        public Builder signOffStation(String signOffStation) {
            this.signOffStation = signOffStation;
            return this;
        }

        public Builder signOffDateTime(LocalDateTime signOffDateTime) {
            this.signOffDateTime = signOffDateTime;
            return this;
        }

        public Builder restHours(Integer restHours) {
            this.restHours = restHours;
            return this;
        }

        public Builder taSno(String taSno) {
            this.taSno = taSno;
            return this;
        }

        public Builder ccId(String ccId) {
            this.ccId = ccId;
            return this;
        }

        public Builder ccUserId(String ccUserId) {
            this.ccUserId = ccUserId;
            return this;
        }

        public Builder signOffApprovalTime(LocalDateTime signOffApprovalTime) {
            this.signOffApprovalTime = signOffApprovalTime;
            return this;
        }

        public Builder signOnNoV(String signOnNoV) {
            this.signOnNoV = signOnNoV;
            return this;
        }

        public Builder transactionTime(LocalDateTime transactionTime) {
            this.transactionTime = transactionTime;
            return this;
        }

        public Builder noOfMeals(Integer noOfMeals) {
            this.noOfMeals = noOfMeals;
            return this;
        }

        public Builder wakeUpTime(LocalDateTime wakeUpTime) {
            this.wakeUpTime = wakeUpTime;
            return this;
        }

        public BookingResponse build() {
            return new BookingResponse(
                    bookingId,
                    occupancyStatus,
                    bedNumber,
                    roomNumber,
                    buildingName,
                    crewId,
                    crewName,
                    crewDesignation,
                    crewType,
                    mealType,
                    vegNonVeg,
                    checkInTime,
                    checkOutTime,
                    subsidizedFood,
                    attachmentPref,
                    signOffStation,
                    signOffDateTime,
                    restHours,
                    taSno,
                    ccId,
                    ccUserId,
                    signOffApprovalTime,
                    signOnNoV,
                    transactionTime,
                    noOfMeals,
                    wakeUpTime
            );
        }
    }
}
