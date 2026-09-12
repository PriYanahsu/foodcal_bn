package com.foodcal.foodcal_backend.repository;

import com.foodcal.foodcal_backend.entity.FoodLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FoodLogRepository extends JpaRepository<FoodLog, UUID> {
}
