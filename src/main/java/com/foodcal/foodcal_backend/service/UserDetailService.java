package com.foodcal.foodcal_backend.service;

import com.foodcal.foodcal_backend.entity.UserDetail;
import com.foodcal.foodcal_backend.exception.InvalidRequestException;
import com.foodcal.foodcal_backend.repository.UserDetailRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
public class UserDetailService {

    private final UserDetailRepository userDetailRepository;
    private final SupabaseStorageService supabaseStorageService;

    public UserDetailService(
            UserDetailRepository userDetailRepository,
            SupabaseStorageService supabaseStorageService
    ){
        this.userDetailRepository = userDetailRepository;
        this.supabaseStorageService = supabaseStorageService;
    }

    public UserDetail updateUser(UserDetail user, UUID userID) {

        UserDetail userDetailDB = userDetailRepository.findById(userID)
                .orElseThrow(() -> new InvalidRequestException("User not found"));

        if (isNotBlank(user.getFullName())) {
            userDetailDB.setFullName(user.getFullName());
        }

        if (isNotBlank(user.getEmail())) {
            userDetailDB.setEmail(user.getEmail());
        }

        if (isNotBlank(user.getAvatarUrl())) {
            userDetailDB.setAvatarUrl(user.getAvatarUrl());
        }

        return userDetailRepository.save(userDetailDB);
    }

    private static boolean isNotBlank(String value){
        return value != null && !value.trim().isEmpty();
    }

    public UserDetail getUserDetails(UUID userID){

        UserDetail userFromDb = userDetailRepository.findById(userID)
                .orElseThrow(() -> new RuntimeException("User NotFound"));

        return userFromDb;
    }

    @Transactional
    public UserDetail uploadAvatar(UUID userID, MultipartFile file) {
        UserDetail user = userDetailRepository.findById(userID)
                .orElseThrow(() -> new InvalidRequestException("User not found"));

        String url = supabaseStorageService.uploadAvatar(userID, file);
        user.setAvatarUrl(url);
        return userDetailRepository.save(user);
    }
}
