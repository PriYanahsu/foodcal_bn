package com.foodcal.foodcal_backend.service;

import com.foodcal.foodcal_backend.exception.InvalidRequestException;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.multipart.MultipartFile;

@Service
public class SupabaseStorageService {

    private static final Logger log = LoggerFactory.getLogger(SupabaseStorageService.class);

    private static final Set<String> ALLOWED_TYPES = Set.of(
        "image/jpeg",
        "image/jpg",
        "image/png",
        "image/webp"
    );

    private static final Map<String, String> EXTENSIONS = Map.of(
        "image/jpeg", "jpg",
        "image/jpg", "jpg",
        "image/png", "png",
        "image/webp", "webp"
    );

    private final RestClient rest;
    private final String publicBaseUrl;
    private final String bucket;

    public SupabaseStorageService(
        @Value("${supabase.url}") String supabaseUrl,
        @Value("${supabase.service-role-key}") String serviceRoleKey,
        @Value("${supabase.avatar-bucket}") String bucket
    ) {
        String base = supabaseUrl.replaceAll("/$", "");
        this.publicBaseUrl = base;
        this.bucket = bucket;
        this.rest = RestClient.builder()
            .baseUrl(base + "/storage/v1")
            .defaultHeader("Authorization", "Bearer " + serviceRoleKey)
            .defaultHeader("apikey", serviceRoleKey)
            .build();
    }

    public String uploadAvatar(UUID userId, MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new InvalidRequestException("File is required");
        }

        String contentType = normalizeContentType(file);
        String filename = "avatar." + EXTENSIONS.get(contentType);
        byte[] bytes;
        try {
            bytes = file.getBytes();
        } catch (IOException e) {
            throw new InvalidRequestException("Could not read uploaded file");
        }

        try {
            rest.post()
                .uri("/object/{bucket}/{userId}/{filename}", bucket, userId.toString(), filename)
                .contentType(MediaType.parseMediaType(contentType.equals("image/jpg") ? "image/jpeg" : contentType))
                .header("x-upsert", "true")
                .body(bytes)
                .retrieve()
                .toBodilessEntity();
        } catch (RestClientResponseException e) {
            log.error("Supabase storage upload failed: {} {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new InvalidRequestException("Failed to upload avatar");
        } catch (RestClientException e) {
            log.error("Supabase storage upload failed", e);
            throw new InvalidRequestException("Failed to upload avatar");
        }

        return publicBaseUrl
            + "/storage/v1/object/public/"
            + bucket
            + "/"
            + userId
            + "/"
            + filename
            + "?t="
            + System.currentTimeMillis();
    }

    private static String normalizeContentType(MultipartFile file) {
        String contentType = file.getContentType();
        if (contentType == null || contentType.isBlank()) {
            throw new InvalidRequestException("Only jpeg/png/webp allowed");
        }
        contentType = contentType.toLowerCase().split(";")[0].trim();
        if (!ALLOWED_TYPES.contains(contentType)) {
            throw new InvalidRequestException("Only jpeg/png/webp allowed");
        }
        return contentType;
    }
}
