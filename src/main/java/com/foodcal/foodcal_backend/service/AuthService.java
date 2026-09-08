package com.foodcal.foodcal_backend.service;

import com.foodcal.foodcal_backend.dto.AuthResponse;
import com.foodcal.foodcal_backend.dto.SignupRequest;
import com.foodcal.foodcal_backend.dto.UserResponse;
import com.foodcal.foodcal_backend.dto.LoginRequest;

import com.foodcal.foodcal_backend.entity.UserDetail;
import com.foodcal.foodcal_backend.exception.DuplicateResourceException;
import com.foodcal.foodcal_backend.exception.InvalidRequestException;
import com.foodcal.foodcal_backend.repository.UserDetailRepository;
import com.foodcal.foodcal_backend.security.JwtUtil;
import com.foodcal.foodcal_backend.security.UserPrincipal;

import java.util.UUID;
import java.util.regex.Pattern;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    public static final String DEFAULT_ROLE = "USER";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );

    private final UserDetailRepository userDetailRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(
        UserDetailRepository userDetailRepository,
        PasswordEncoder passwordEncoder,
        JwtUtil jwtUtil
    ) {
        this.userDetailRepository = userDetailRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Transactional
    public AuthResponse signup(SignupRequest request) {
        validate(request);

        String email = request.getEmail().trim().toLowerCase();

        if (userDetailRepository.existsByEmailIgnoreCase(email)) {
            throw new DuplicateResourceException("Email already registered");
        }

        UserDetail user = new UserDetail();
        user.setFullName(request.getFullName().trim());
        user.setEmail(email);
        user.setRole(DEFAULT_ROLE);
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));

        UserDetail saved = userDetailRepository.save(user);

        UserPrincipal principal = new UserPrincipal(
            saved.getId(),
            saved.getEmail(),
            saved.getFullName(),
            saved.getRole()
        );

        AuthResponse response = new AuthResponse();
        response.setAccessToken(jwtUtil.createAccessToken(principal));
        response.setUser(toUserResponse(saved));
        return response;
    }

    public AuthResponse login(LoginRequest request) {

        String email = request.getEmail().trim().toLowerCase();
        String reqPassword = request.getPassword();

        UserDetail user = userDetailRepository.findByEmailIgnoreCase(email);

        if(user == null) {
            throw new InvalidRequestException("Invalid email or password");
        }

        if(!passwordEncoder.matches(reqPassword, user.getPasswordHash())) {
            throw new InvalidRequestException("Invalid email or password");
        }

        UserPrincipal principal = new UserPrincipal(
            user.getId(),
            user.getEmail(),
            user.getFullName(),
            user.getRole()
        );

        AuthResponse response = new AuthResponse();
        response.setAccessToken(jwtUtil.createAccessToken(principal));
        response.setUser(toUserResponse(user));
        return response;
    }

    public void deleteUser(UUID userID) {
        if (!userDetailRepository.existsById(userID)) {
            throw new RuntimeException("User not found");
        }
        userDetailRepository.deleteById(userID);
    }

    private static void validate(SignupRequest request) {
        if (request == null) {
            throw new InvalidRequestException("Request body is required");
        }
        if (isBlank(request.getFullName())) {
            throw new InvalidRequestException("Full name is required");
        }
        if (isBlank(request.getEmail()) || !EMAIL_PATTERN.matcher(request.getEmail().trim()).matches()) {
            throw new InvalidRequestException("A valid email is required");
        }
        if (isBlank(request.getPassword()) || request.getPassword().length() < 8) {
            throw new InvalidRequestException("Password must be at least 8 characters");
        }
        if (request.getPassword().length() > 72) {
            throw new InvalidRequestException("Password must be at most 72 characters");
        }
    }

    private static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private static UserResponse toUserResponse(UserDetail user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setFullName(user.getFullName());
        response.setEmail(user.getEmail());
        response.setAvatarUrl(user.getAvatarUrl());
        response.setRole(user.getRole());
        return response;
    }

    private static String trimToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
