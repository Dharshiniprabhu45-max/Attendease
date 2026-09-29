package com.attendease.attendease.service;

import com.attendease.attendease.dto.LoginRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Value("${attendease.auth.username:admin}")
    private String configuredUsername;

    @Value("${attendease.auth.password:admin123}")
    private String configuredPassword;

    public boolean isValid(LoginRequest request) {

        if (request == null
                || request.getUsername() == null
                || request.getPassword() == null) {
            return false;
        }

        return configuredUsername.equals(request.getUsername().trim())
                && configuredPassword.equals(request.getPassword());
    }
}