package com.infyniteloop.isec.security.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
@Builder
public class UserInfoResponse {
    private UUID id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private boolean accountNonLocked;
    private boolean accountNonExpired;
    private boolean credentialsNonExpired;
    private boolean enabled;
    private LocalDate credentialsExpiryDate;
    private LocalDate accountExpiryDate;
    private boolean isTwoFactorEnabled;
    private String location;
    private String division;
    private String zone;
    private List<String> roles;

    //TODO: Remove this constructor or use Builder pattern
    public UserInfoResponse(UUID id, String username, String email, String firstName, String lastName, String phone, boolean accountNonLocked, boolean accountNonExpired,
                            boolean credentialsNonExpired, boolean enabled, LocalDate credentialsExpiryDate,
                            LocalDate accountExpiryDate, boolean isTwoFactorEnabled, List<String> roles) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.accountNonLocked = accountNonLocked;
        this.accountNonExpired = accountNonExpired;
        this.credentialsNonExpired = credentialsNonExpired;
        this.enabled = enabled;
        this.credentialsExpiryDate = credentialsExpiryDate;
        this.accountExpiryDate = accountExpiryDate;
        this.isTwoFactorEnabled = isTwoFactorEnabled;
        this.roles = roles;
    }

    public UserInfoResponse(
            UUID userId,
            @NotBlank @Size(max = 20) String userName,
            @NotBlank(message = "email is required") @Size(max = 50) @Email String email,
            @NotBlank(message = "First name is required") @Size(max = 50) String firstName,
            @NotBlank(message = "Last name is required") @Size(max = 50) String lastName,
            @NotBlank(message = "Phone number is required") @Size(min = 10, max = 10, message = "Phone number must be exactly 10 digits") @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must contain only digits") String phone,
            boolean accountNonLocked,
            boolean accountNonExpired,
            boolean credentialsNonExpired,
            boolean enabled,
            LocalDate credentialsExpiryDate,
            LocalDate accountExpiryDate,
            boolean isTwoFactorEnabled,
            String location,
            String division,
            String zone,
            List<String> roles) {

        this.id = userId;
        this.username = userName;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.accountNonLocked = accountNonLocked;
        this.accountNonExpired = accountNonExpired;
        this.credentialsNonExpired = credentialsNonExpired;
        this.enabled = enabled;
        this.credentialsExpiryDate = credentialsExpiryDate;
        this.accountExpiryDate = accountExpiryDate;
        this.isTwoFactorEnabled = isTwoFactorEnabled;
        this.location = location;
        this.division = division;
        this.zone = zone;
        this.roles = roles;
    }
}
