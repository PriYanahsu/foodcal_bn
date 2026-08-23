package com.foodcal.foodcal_backend.security;

import java.util.UUID;

/**
 * The logged-in user as stored in a JWT (id, email, username, role).
 * This is what controllers can read from the security context after a valid token.
 */
public class UserPrincipal {

    private final UUID id;
    private final String email;
    private final String username;
    private final String role;

    public UserPrincipal(UUID id, String email, String username, String role){
        this.id = id;
        this.email = email;
        this.username = username;
        this.role = role;
    }

    public UUID getId() {
        return id;
    }
    
    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }   

}