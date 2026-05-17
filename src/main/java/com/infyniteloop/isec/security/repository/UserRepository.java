package com.infyniteloop.isec.security.repository;


import com.infyniteloop.isec.security.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUserName(String username);

    boolean existsByUserName(String username);
    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    // Find all users that belong to a specific tenant
    List<User> findAllByTenantId(UUID tenantId);

    // Pageable variant for large result sets
    org.springframework.data.domain.Page<User> findAllByTenantId(UUID tenantId, org.springframework.data.domain.Pageable pageable);

}

