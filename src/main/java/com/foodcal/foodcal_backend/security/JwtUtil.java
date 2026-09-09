package com.foodcal.foodcal_backend.security;

import com.foodcal.foodcal_backend.dto.RefreshTokenResponse;
import com.foodcal.foodcal_backend.entity.UserDetail;
import com.foodcal.foodcal_backend.repository.UserDetailRepository;
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

    private final UserDetailRepository userDetailRepository;

    public JwtUtil(UserDetailRepository userDetailRepository) {
        this.userDetailRepository = userDetailRepository;
    }

    // Secret used to sign and verify tokens. Must stay private and match jwt.secret.
    @Value("${jwt.secret}")
    private String secret;

    // How long a token stays valid, in milliseconds.
    @Value("${jwt.access.expiration}")
    private Long accessExpiration;

    // How long a token stays valid, in milliseconds.
    @Value("${jwt.refresh.expiration}")
    private Long refreshExpiration;

    /** Creates a signed token for a logged-in user. */
    public String createAccessToken(UserPrincipal userPrincipal) {
        return Jwts.builder()
            .setSubject(userPrincipal.getId().toString())
            .claim("id", userPrincipal.getId().toString())
            .claim("email", userPrincipal.getEmail())
            .claim("username", userPrincipal.getUserName())
            .claim("role", userPrincipal.getRole())
            .claim("type", "access")
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + accessExpiration))
            .signWith(Keys.hmacShaKeyFor(secret.getBytes()), SignatureAlgorithm.HS256)
            .compact();
    }

    public String createRefreshToken(UserPrincipal userPrincipal) {
        return Jwts.builder()
                .setSubject(userPrincipal.getId().toString())
                .claim("id", userPrincipal.getId().toString())
                .claim("type", "refresh")
                .setIssuedAt(new Date())
                .setExpiration(
                        new Date(System.currentTimeMillis() + refreshExpiration)
                )
                .signWith(
                        Keys.hmacShaKeyFor(secret.getBytes()),
                        SignatureAlgorithm.HS256
                )
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

    // create refresh and access refresh page
    public RefreshTokenResponse createAccessTokenFromRefreshToken(
            String refreshToken
    ) {

        try {
            Claims claims = parseClaims(refreshToken);
            // Make sure this is actually a refresh token
            String type = claims.get("type", String.class);

            if (!"refresh".equals(type)) {
                throw new RuntimeException("Invalid refresh token");
            }

            UUID userId = UUID.fromString(
                    claims.get("id", String.class)
            );

            // Get user from DB
            UserDetail user = userDetailRepository.findById(userId)
                    .orElseThrow(() ->
                            new RuntimeException("User not found")
                    );

            UserPrincipal userPrincipal = new UserPrincipal(
                    user.getId(),
                    user.getEmail(),
                    user.getFullName(),
                    user.getRole().toString()
            );

            // Create NEW access token
            String newAccessToken =
                    createAccessToken(userPrincipal);

            // You can either keep the old refresh token
            // or create a new one (rotation)
            String newRefreshToken =
                    createRefreshToken(userPrincipal);

            RefreshTokenResponse response = new RefreshTokenResponse();

            response.setAccessToken(newAccessToken);
            response.setRefreshToken(newRefreshToken);

            return response;

        } catch (Exception e) {
            throw new RuntimeException("Invalid or expired refresh token");
        }
    }
}
