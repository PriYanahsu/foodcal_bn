package com.foodcal.foodcal_backend.service;

import com.foodcal.foodcal_backend.entity.FoodLog;
import com.foodcal.foodcal_backend.entity.UserDetail;
import com.foodcal.foodcal_backend.repository.FoodLogRepository;
import com.foodcal.foodcal_backend.repository.UserDetailRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
public class FoodService {

    private UserDetailRepository userDetailRepository;
    private SupabaseStorageService supabaseStorageService;
    private FoodLogRepository foodLogRepository;

    public FoodService(
            UserDetailRepository userDetailRepository,
            SupabaseStorageService supabaseStorageService,
            FoodLogRepository foodLogRepository
    ){
        this.userDetailRepository = userDetailRepository;
        this.supabaseStorageService = supabaseStorageService;
        this.foodLogRepository = foodLogRepository;
    }

    public FoodLog addFoodService(
            UUID userID,
            MultipartFile file,
            FoodLog foodData
    ){
        UserDetail user = userDetailRepository.findById(userID)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String url = supabaseStorageService.uploadFoodLog(userID,file);
        foodData.setImagePath(url);
        foodData.setUser(user);
        return foodLogRepository.save(foodData);
    }
}
