package com.infyniteloop.isec.security.services;

public interface EmailService {
    void sendPasswordResetEmail(String to, String resetUrl);
}
