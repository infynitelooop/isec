package com.infyniteloop.isec.security.controller;

import com.infyniteloop.isec.security.dtos.UserRequest;
import com.infyniteloop.isec.security.dtos.UserResponse;
import com.infyniteloop.isec.security.models.Role;
import com.infyniteloop.isec.security.models.User;
import com.infyniteloop.isec.security.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin")
//@PreAuthorize("hasRole('ROLE_ADMIN')")
//@Secured("ROLE_ADMIN")
@Tag(name = "Admin Controller", description = "APIs for admin operations")
public class AdminController {

    @Autowired
    UserService userService;


    @GetMapping("/getusers")
    @Operation(summary = "Get all users (Admin only)")
    public ResponseEntity<List<User>> getAllUsers() {
        return new ResponseEntity<>(userService.getAllUsers(),
                HttpStatus.OK);
    }

    @PutMapping("/update-user/{userId}")
    @Operation(summary = "Update user (Admin only)")
    public ResponseEntity<String> updateUser(@PathVariable UUID userId,
                                             @Valid @RequestBody UserRequest userRequest) {
        userService.updateUser(userId, userRequest);
        return ResponseEntity.ok("User role updated");
    }

    @GetMapping("/user/{id}")
    @Operation(summary = "Get user by ID (Admin only)")
    public ResponseEntity<UserResponse> getUser(@PathVariable UUID id) {
        return new ResponseEntity<>(userService.getUserById(id),
                HttpStatus.OK);
    }

    @PutMapping("/update-lock-status")
    @Operation(summary = "Update account lock status (Admin only)")
    public ResponseEntity<String> updateAccountLockStatus(@RequestParam UUID userId,
                                                          @RequestParam boolean lock) {
        userService.updateAccountLockStatus(userId, lock);
        return ResponseEntity.ok("Account lock status updated");
    }

    @GetMapping("/roles")
    @Operation(summary = "Get all roles (Admin only)")
    public List<Role> getAllRoles() {
        return userService.getAllRoles();
    }

    @PutMapping("/update-expiry-status")
    @Operation(summary = "Update account expiry status (Admin only)")
    public ResponseEntity<String> updateAccountExpiryStatus(@RequestParam UUID userId,
                                                            @RequestParam boolean expire) {
        userService.updateAccountExpiryStatus(userId, expire);
        return ResponseEntity.ok("Account expiry status updated");
    }

    @PutMapping("/update-enabled-status")
    @Operation(summary = "Update account enabled status (Admin only)")
    public ResponseEntity<String> updateAccountEnabledStatus(@RequestParam UUID userId,
                                                             @RequestParam boolean enabled) {
        userService.updateAccountEnabledStatus(userId, enabled);
        return ResponseEntity.ok("Account enabled status updated");
    }

    @PutMapping("/update-credentials-expiry-status")
    @Operation(summary = "Update credentials expiry status (Admin only)")
    public ResponseEntity<String> updateCredentialsExpiryStatus(@RequestParam UUID userId, @RequestParam boolean expire) {
        userService.updateCredentialsExpiryStatus(userId, expire);
        return ResponseEntity.ok("Credentials expiry status updated");
    }

    @PutMapping("/update-password")
    @Operation(summary = "Update user password (Admin only)")
    public ResponseEntity<String> updatePassword(@RequestParam UUID userId,
                                                 @RequestParam String password) {
        try {
            userService.updatePassword(userId, password);
            return ResponseEntity.ok("Password updated");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
