package com.foodcal.foodcal_backend.dto;

public class LoginRequest {

    private String email;

    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}