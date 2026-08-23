package com.foodcal.foodcal_backend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Builds and reads JWT access tokens.
 * A token is a signed string that carries user id, email, username, and role.
 */
@Component
public class JwtUtil {

    // Secret used to sign and verify tokens. Must stay private and match jwt.secret.
    @Value("${jwt.secret}")
    private String secret;

    // How long a token stays valid, in milliseconds (from jwt.expiration).
    @Value("${jwt.expiration}")
    private Long expiration;

    /** Creates a signed token for a logged-in user. */
    public String createAccessToken(UserPrincipal userPrincipal) {
        return Jwts.builder()
            .setSubject(userPrincipal.getId().toString())
            .claim("id", userPrincipal.getId().toString())
            .claim("email", userPrincipal.getEmail())
            .claim("username", userPrincipal.getUsername())
            .claim("role", userPrincipal.getRole())
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + expiration))
            .signWith(Keys.hmacShaKeyFor(secret.getBytes()), SignatureAlgorithm.HS256)
            .compact();
    }

    /** Returns true if the token is signed correctly and has not expired. */
    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /** Reads user details stored inside a valid token. */
    public UserPrincipal extractUserPrincipal(String token) {
        Claims claims = parseClaims(token);
        return new UserPrincipal(
            UUID.fromString(claims.get("id").toString()),
            claims.get("email").toString(),
            claims.get("username").toString(),
            claims.get("role").toString()
        );
    }

    // Claims are the fields inside the token (id, email, username, role, expiry).
    private Claims parseClaims(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes()))
            .build()
            .parseClaimsJws(token)
            .getBody();
    }
}
