package com.infyniteloop.isec.security.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;


public record UserRequest(

        @Size(max = 20, message = "Username must not exceed 20 characters")
        String userName,

        @Email(message = "Invalid email format")
        @Size(max = 50, message = "Email must not exceed 50 characters")
        String email,

        @Size(max = 50, message = "firstName must not exceed 50 characters")
        String firstName,

        @Size(max = 50, message = "lastName must not exceed 50 characters")
        String lastName,

        @NotBlank(message = "Phone number is required")
        @Size(min = 10, max = 10, message = "Phone number must be exactly 10 digits")
        @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must contain only digits")
        String phone,

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
