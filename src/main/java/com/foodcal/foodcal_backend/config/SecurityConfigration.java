package com.foodcal.foodcal_backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.CorsConfiguration;
import java.util.Arrays;
import java.util.List;
import com.foodcal.foodcal_backend.security.JwtAuthFilter;

@Configuration
public class SecurityConfigration {

    @Value("${env.frontend.url}")
    private String frontendUrl;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthFilter jwtAuthFilter)
        throws Exception {
        http.csrf(c -> c.disable())
            .cors(c -> c.configurationSource(corsConfigurationSource()))
            .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth ->
                auth
                    .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                    .requestMatchers("/api/v1/auth/signup").permitAll()
                    .requestMatchers("/api/v1/auth/login").permitAll()
                    .requestMatchers("/api/v1/auth/refresh-token").permitAll()
                    .requestMatchers("/api/v1/auth/test").permitAll()
                    .requestMatchers("/api/v1/admin/**").hasAuthority("ADMIN")
                    .requestMatchers("/api/v1/**").authenticated()
                    .anyRequest().denyAll()
            )
            // Missing/expired token -> 401 (Spring defaults to 403), so the client
            // knows to refresh the token or send the user back to sign in.
            .exceptionHandling(e -> e.authenticationEntryPoint(
                new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)
            ))
            // Check JWT before username/password auth. Login/register stay public.
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Trimmed, so "https://a.app, https://b.app" in the env var still matches both origins.
        List<String> allowedOrigins = Arrays.stream(frontendUrl.split(","))
            .map(String::trim)
            .map(origin -> origin.replaceAll("/+$", ""))
            .filter(origin -> !origin.isEmpty())
            .toList();
        configuration.setAllowedOrigins(allowedOrigins);
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
