package com.infyniteloop.isec.security.services;



import com.infyniteloop.isec.security.dtos.UserDTO;
import com.infyniteloop.isec.security.dtos.UserRequest;
import com.infyniteloop.isec.security.dtos.UserResponse;
import com.infyniteloop.isec.security.models.Role;
import com.infyniteloop.isec.security.models.User;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;

import java.util.List;
import java.util.UUID;

public interface UserService {

    UserResponse updateUser(UUID userId, UserRequest request);

    void updateUserRoles(UUID userId, List<String> roleNames);

    List<User> getAllUsers();

    UserResponse getUserById(UUID id);

    User findByUsername(String username);

    void updateAccountLockStatus(UUID userId, boolean lock);

    List<Role> getAllRoles();

    void updateAccountExpiryStatus(UUID userId, boolean expire);

    void updateAccountEnabledStatus(UUID userId, boolean enabled);

    void updateCredentialsExpiryStatus(UUID userId, boolean expire);

    void updatePassword(UUID userId, String password);

    void generatePasswordResetToken(String email);

    void resetPassword(String token, String newPassword);

    GoogleAuthenticatorKey generate2FASecret(UUID userId);

    boolean validate2FACode(UUID userId, int code);

    void enable2FA(UUID userId);

    void disable2FA(UUID userId);
}
