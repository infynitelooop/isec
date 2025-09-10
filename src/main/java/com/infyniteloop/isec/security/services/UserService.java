package com.infyniteloop.isec.security.services;



import com.infyniteloop.isec.security.dtos.UserDTO;
import com.infyniteloop.isec.security.models.Role;
import com.infyniteloop.isec.security.models.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    void updateUserRole(UUID userId, String roleName);

    List<User> getAllUsers();

    UserDTO getUserById(UUID id);

    User findByUsername(String username);

    void updateAccountLockStatus(UUID userId, boolean lock);

    List<Role> getAllRoles();

    void updateAccountExpiryStatus(UUID userId, boolean expire);

    void updateAccountEnabledStatus(UUID userId, boolean enabled);

    void updateCredentialsExpiryStatus(UUID userId, boolean expire);

    void updatePassword(UUID userId, String password);

    void generatePasswordResetToken(String email);

    void resetPassword(String token, String newPassword);
}
