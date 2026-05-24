package com.infyniteloop.runningroom.runningroom.repository;

import com.infyniteloop.runningroom.runningroom.entity.Division;
import com.infyniteloop.runningroom.runningroom.entity.Zone;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface DivisionRepository extends JpaRepository<Division, UUID>  {
    Optional<Division> findByName(String name);
}
