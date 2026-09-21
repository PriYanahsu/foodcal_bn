package com.foodcal.foodcal_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.foodcal.foodcal_backend.entity.Weight;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WeightRepository extends JpaRepository<Weight, UUID> {

    List<Weight> findByUserId(UUID userId);

    Optional<Weight> findByUserIdAndLoggedOn(UUID userId, LocalDate loggedOn);
}