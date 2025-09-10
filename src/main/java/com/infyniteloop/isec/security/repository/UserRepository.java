package com.infyniteloop.isec.security.repository;


import com.infyniteloop.isec.security.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUserName(String username);

    boolean existsByUserName(String username);
    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);
}

