package com.infyniteloop.isec.security.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;


public record UserRequest(

        @Size(max = 20, message = "Username must not exceed 20 characters")
        String userName,

        @Email(message = "Invalid email format")
        @Size(max = 50, message = "Email must not exceed 50 characters")
        String email,

        Boolean accountNonLocked,
        Boolean accountNonExpired,
        Boolean credentialsNonExpired,
        Boolean enabled,

        LocalDate credentialsExpiryDate,
        LocalDate accountExpiryDate,

        Boolean isTwoFactorEnabled,
        String twoFactorSecret,

        // Pass roles as list of role names
        List<String> roles
) {}
