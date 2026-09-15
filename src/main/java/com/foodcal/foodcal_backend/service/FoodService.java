package com.foodcal.foodcal_backend.service;

import com.foodcal.foodcal_backend.dto.FoodLogStatsDateResponse;
import com.foodcal.foodcal_backend.entity.FoodLog;
import com.foodcal.foodcal_backend.entity.UserDetail;
import com.foodcal.foodcal_backend.repository.FoodLogRepository;
import com.foodcal.foodcal_backend.repository.UserDetailRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.*;

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
        foodData.setDate(LocalDate.now());
        return foodLogRepository.save(foodData);
    }

    public FoodLogStatsDateResponse getStatsFoodLog(UUID userID, LocalDate date){

        List<FoodLog> allFoodLogUser = foodLogRepository.findByUserIdAndDate(userID, date);

        FoodLogStatsDateResponse newFoodLog = new FoodLogStatsDateResponse();
        boolean AlreadyDatePresentAsKey = false;

        for(FoodLog foodLog : allFoodLogUser){
            incrementMacros(foodLog, newFoodLog, AlreadyDatePresentAsKey);
            AlreadyDatePresentAsKey = true;
        }

        return newFoodLog;
    }



    public List<FoodLog> getFoodLog(UUID userId, LocalDate date){

        List<FoodLog> allFoods = foodLogRepository.findByUserIdAndDate(userId, date);

        Map<LocalDate, FoodLogStatsDateResponse> getAllUserDataUsingDate = new TreeMap<>();

        for(FoodLog foodlog : allFoods){
            calculatePerDayMacros(foodlog, getAllUserDataUsingDate);
        }

        return allFoods;
    }

    public Map<LocalDate, FoodLogStatsDateResponse> calculatePerDayMacros(
            FoodLog foodlog,
            Map<LocalDate, FoodLogStatsDateResponse> getAllUserDataUsingDate
    ){

        Boolean AlreadyDatePresentAsKey;
        if(getAllUserDataUsingDate.containsKey(foodlog.getDate())){
            AlreadyDatePresentAsKey = true;
            getAllUserDataUsingDate.put(
                    foodlog.getDate(), incrementMacros(
                            foodlog,
                            getAllUserDataUsingDate.get(foodlog.getDate()),
                            AlreadyDatePresentAsKey
                            ));
        }else{
            FoodLogStatsDateResponse newFoodLog = new FoodLogStatsDateResponse();
            AlreadyDatePresentAsKey = false;
            getAllUserDataUsingDate.put(
                    foodlog.getDate(), incrementMacros(
                            foodlog,
                            newFoodLog,
                            AlreadyDatePresentAsKey
                    ));
        }

        return getAllUserDataUsingDate;
    }

    public FoodLogStatsDateResponse incrementMacros(
            FoodLog foodlog,
            FoodLogStatsDateResponse currentMacros,
            Boolean AlreadyDatePresentAsKey
    ){
        Double calories = 0.0;
        Double proteins = 0.0;
        Double carbs = 0.0;
        Double fats = 0.0;

        if(AlreadyDatePresentAsKey){
            calories = currentMacros.getCalories();
            proteins = currentMacros.getProteins();
            carbs = currentMacros.getCarbohydrates();
            fats = currentMacros.getFats();
        }

        calories += foodlog.getCalories();
        proteins += foodlog.getProteinG();
        carbs += foodlog.getCarbohydrateG();
        fats += foodlog.getFatG();

        currentMacros.setCalories(calories);
        currentMacros.setProteins(proteins);
        currentMacros.setCarbohydrates(carbs);
        currentMacros.setFats(fats);

        return currentMacros;
    }

}
