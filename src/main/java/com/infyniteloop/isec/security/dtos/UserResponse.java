package com.infyniteloop.isec.security.dtos;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public record UserResponse(
        UUID userId,
        String userName,
        String email,
        boolean accountNonLocked,
        boolean accountNonExpired,
        boolean credentialsNonExpired,
        boolean enabled,
        LocalDate credentialsExpiryDate,
        LocalDate accountExpiryDate,
        boolean isTwoFactorEnabled,
        String signUpMethod,
        Set<String> roles,
        UUID tenantId,
        LocalDateTime createdDate,
        LocalDateTime updatedDate
) {}
