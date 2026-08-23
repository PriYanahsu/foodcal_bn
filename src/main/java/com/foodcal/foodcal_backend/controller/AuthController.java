package com.foodcal.foodcal_backend.controller;

import com.foodcal.foodcal_backend.dto.AuthResponse;
import com.foodcal.foodcal_backend.dto.SignupRequest;
import com.foodcal.foodcal_backend.dto.LoginRequest;
import com.foodcal.foodcal_backend.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(@RequestBody SignupRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.signup(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(authService.login(request));
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.status(HttpStatus.OK).body("Hello World");
    }
}
