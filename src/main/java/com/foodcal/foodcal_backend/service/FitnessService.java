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

    public FitnessDetail addFitnessDetail(
            FitnessDetail fitnessDetail,
            UUID userId
    ) {
        UserDetail user = userDetailRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User does not exist"));

        if (fitnessDetailRepository.existsByUserId(userId)) {
            throw new RuntimeException("Fitness details already exist for this user");
        }

        FitnessDetail fitnessDetailDB = new FitnessDetail();
        fitnessDetailDB.setUser(user);
        copyFitnessFields(fitnessDetailDB, fitnessDetail);

        return fitnessDetailRepository.save(fitnessDetailDB);
    }

    public FitnessDetail updateFitnessDetail(
            UUID userID,
            UUID fitnessID,
            FitnessDetail fitnessDetail
    ){
        FitnessDetail fitnessDetailDB = fitnessDetailRepository.findByIdAndUserId(fitnessID, userID)
                .orElseThrow(() -> new RuntimeException("Fitness detail not match with user"));

        copyFitnessFields(fitnessDetailDB, fitnessDetail);
        return fitnessDetailRepository.save(fitnessDetailDB);
    }

    public void copyFitnessFields(
            FitnessDetail fitnessDetailDB,
            FitnessDetail fitnessDetail
    ){
        fitnessDetailDB.setAge(fitnessDetail.getAge());
        fitnessDetailDB.setHeight(fitnessDetail.getHeight());
        fitnessDetailDB.setWeight(fitnessDetail.getWeight());
        fitnessDetailDB.setActivityLevel(fitnessDetail.getActivityLevel());
        fitnessDetailDB.setTargetWeightKg(fitnessDetail.getTargetWeightKg());
        fitnessDetailDB.setTargetDate(fitnessDetail.getTargetDate());
        fitnessDetailDB.setDailyCalorieTarget(fitnessDetail.getDailyCalorieTarget());
        fitnessDetailDB.setDailyProteinTargetG(fitnessDetail.getDailyProteinTargetG());
        fitnessDetailDB.setDailyCarbsTargetG(fitnessDetail.getDailyCarbsTargetG());
        fitnessDetailDB.setDailyFatTargetG(fitnessDetail.getDailyFatTargetG());
        fitnessDetailDB.setAiCoachAdvice(fitnessDetail.getAiCoachAdvice());
    }
}