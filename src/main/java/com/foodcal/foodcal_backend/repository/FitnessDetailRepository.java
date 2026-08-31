package com.foodcal.foodcal_backend.repository;

import com.foodcal.foodcal_backend.entity.FitnessDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface FitnessDetailRepository extends JpaRepository<FitnessDetail, UUID> {

    boolean existsByUserId(UUID userID);

    Optional<FitnessDetail> findByIdAndUserId(UUID fitnessId, UUID userId);
}
