package com.foodcal.foodcal_backend.service;

import com.foodcal.foodcal_backend.exception.DuplicateResourceException;
import com.foodcal.foodcal_backend.entity.UserDetail;
import com.foodcal.foodcal_backend.entity.Weight;
import com.foodcal.foodcal_backend.repository.UserDetailRepository;
import com.foodcal.foodcal_backend.repository.WeightRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WeightService {

    private final WeightRepository weightRepository;
    private final UserDetailRepository userDetailRepository;

    public WeightService(
        WeightRepository weightRepository,
        UserDetailRepository userDetailRepository
    ) {
        this.weightRepository = weightRepository;
        this.userDetailRepository = userDetailRepository;
    }

    @Transactional
    public ResponseEntity<Weight> logWeight(Weight weight, UUID userId) {
        UserDetail user = userDetailRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));

        LocalDate today = LocalDate.now();
        if (weightRepository.findByUserIdAndLoggedOn(userId, today).isPresent()) {
            throw new DuplicateResourceException("You've already logged today's weight.");
        }

        weight.setLoggedOn(today);
        weight.setUser(user);
        weightRepository.save(weight);

        return ResponseEntity.ok(weight);
    }

    public ResponseEntity<List<Weight>> getWeights(UUID userId) {
        List<Weight> weights = weightRepository.findByUserId(userId);
        return ResponseEntity.ok(weights);
    }
}
