package com.foodcal.foodcal_backend.service;

import com.foodcal.foodcal_backend.entity.FitnessDetail;
import com.foodcal.foodcal_backend.entity.UserDetail;
import com.foodcal.foodcal_backend.repository.FitnessDetailRepository;
import com.foodcal.foodcal_backend.repository.UserDetailRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class FitnessService {

    private final UserDetailRepository userDetailRepository;
    private final FitnessDetailRepository fitnessDetailRepository;

    public FitnessService(
            UserDetailRepository userDetailRepository,
            FitnessDetailRepository fitnessDetailRepository
    ) {
        this.userDetailRepository = userDetailRepository;
        this.fitnessDetailRepository = fitnessDetailRepository;
    }

    public FitnessDetail getFitnessDetail(
            UUID userId
    ) {
        userDetailRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User does not exist"));

        return fitnessDetailRepository.findByUserId(userId)
                .orElse(null);
    }

    public FitnessDetail updateFitnessDetail(
            UUID userID,
            FitnessDetail fitnessDetail
    ){
        UserDetail user = userDetailRepository.findById(userID)
                .orElseThrow(() -> new RuntimeException("User does not exist"));

        FitnessDetail fitnessDetailDB = fitnessDetailRepository.findByUserId(userID)
                .orElseGet(() -> {
                    FitnessDetail created = new FitnessDetail();
                    created.setUser(user);
                    return created;
                });
        copyFitnessFields(fitnessDetailDB, fitnessDetail);
        return fitnessDetailRepository.save(fitnessDetailDB);
    }

    public void copyFitnessFields(
            FitnessDetail fitnessDetailDB,
            FitnessDetail fitnessDetail
    ) {
        if (fitnessDetail.getAge() != null) {
            fitnessDetailDB.setAge(fitnessDetail.getAge());
        }

        if (fitnessDetail.getHeight() != null) {
            fitnessDetailDB.setHeight(fitnessDetail.getHeight());
        }

        if (fitnessDetail.getWeight() != null) {
            fitnessDetailDB.setWeight(fitnessDetail.getWeight());
        }

        if (fitnessDetail.getActivityLevel() != null) {
            fitnessDetailDB.setActivityLevel(fitnessDetail.getActivityLevel());
        }

        if (fitnessDetail.getTargetWeightKg() != null) {
            fitnessDetailDB.setTargetWeightKg(fitnessDetail.getTargetWeightKg());
        }

        if (fitnessDetail.getTargetDate() != null) {
            fitnessDetailDB.setTargetDate(fitnessDetail.getTargetDate());
        }

        if (fitnessDetail.getDailyCalorieTarget() != null) {
            fitnessDetailDB.setDailyCalorieTarget(
                    fitnessDetail.getDailyCalorieTarget()
            );
        }

        if (fitnessDetail.getDailyProteinTargetG() != null) {
            fitnessDetailDB.setDailyProteinTargetG(
                    fitnessDetail.getDailyProteinTargetG()
            );
        }

        if (fitnessDetail.getDailyCarbsTargetG() != null) {
            fitnessDetailDB.setDailyCarbsTargetG(
                    fitnessDetail.getDailyCarbsTargetG()
            );
        }

        if (fitnessDetail.getDailyFatTargetG() != null) {
            fitnessDetailDB.setDailyFatTargetG(
                    fitnessDetail.getDailyFatTargetG()
            );
        }

        if (fitnessDetail.getAiCoachAdvice() != null) {
            fitnessDetailDB.setAiCoachAdvice(
                    fitnessDetail.getAiCoachAdvice()
            );
        }

        if(fitnessDetail.getObjective() != null){
            fitnessDetailDB.setObjective(
                    fitnessDetail.getObjective()
            );
        }

        if(fitnessDetail.getGender() != null){
            fitnessDetailDB.setGender(
                    fitnessDetail.getGender()
            );
        }
    }
}