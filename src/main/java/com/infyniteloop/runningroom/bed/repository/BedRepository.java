package com.infyniteloop.runningroom.bed.repository;

<<<<<<< Updated upstream:src/main/java/com/infyniteloop/runningroom/repository/BedRepository.java
import com.infyniteloop.runningroom.model.Bed;
=======
import com.infyniteloop.runningroom.bed.entity.Bed;
import com.infyniteloop.runningroom.room.entity.Room;
>>>>>>> Stashed changes:src/main/java/com/infyniteloop/runningroom/bed/repository/BedRepository.java
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface BedRepository extends JpaRepository<Bed, UUID> {
    Optional<Bed> findByBedNumber(String bedNumber);
    boolean existsByBedNumberAndRoomId(String bedNumber, UUID roomId);
}