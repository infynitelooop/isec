package com.infyniteloop.isec.security.controller;

import com.infyniteloop.isec.security.dtos.UserDTO;
import com.infyniteloop.isec.security.models.Role;
import com.infyniteloop.isec.security.models.User;
import com.infyniteloop.isec.security.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PutMapping("/update-role")
    @Operation(summary = "Update user role (Admin only)")
    public ResponseEntity<String> updateUserRole(@RequestParam Long userId, 
                                                 @RequestParam String roleName) {
        userService.updateUserRole(userId, roleName);
        return ResponseEntity.ok("User role updated");
    }

    @GetMapping("/user/{id}")
    @Operation(summary = "Get user by ID (Admin only)")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long id) {
        return new ResponseEntity<>(userService.getUserById(id),
                HttpStatus.OK);
    }

    @PutMapping("/update-lock-status")
    @Operation(summary = "Update account lock status (Admin only)")
    public ResponseEntity<String> updateAccountLockStatus(@RequestParam Long userId,
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
    public ResponseEntity<String> updateAccountExpiryStatus(@RequestParam Long userId,
                                                            @RequestParam boolean expire) {
        userService.updateAccountExpiryStatus(userId, expire);
        return ResponseEntity.ok("Account expiry status updated");
    }

    @PutMapping("/update-enabled-status")
    @Operation(summary = "Update account enabled status (Admin only)")
    public ResponseEntity<String> updateAccountEnabledStatus(@RequestParam Long userId,
                                                             @RequestParam boolean enabled) {
        userService.updateAccountEnabledStatus(userId, enabled);
        return ResponseEntity.ok("Account enabled status updated");
    }

    @PutMapping("/update-credentials-expiry-status")
    @Operation(summary = "Update credentials expiry status (Admin only)")
    public ResponseEntity<String> updateCredentialsExpiryStatus(@RequestParam Long userId, @RequestParam boolean expire) {
        userService.updateCredentialsExpiryStatus(userId, expire);
        return ResponseEntity.ok("Credentials expiry status updated");
    }

    @PutMapping("/update-password")
    @Operation(summary = "Update user password (Admin only)")
    public ResponseEntity<String> updatePassword(@RequestParam Long userId,
                                                 @RequestParam String password) {
        try {
            userService.updatePassword(userId, password);
            return ResponseEntity.ok("Password updated");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
