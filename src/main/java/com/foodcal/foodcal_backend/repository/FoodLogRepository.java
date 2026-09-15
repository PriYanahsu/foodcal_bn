package com.foodcal.foodcal_backend.repository;

import com.foodcal.foodcal_backend.entity.FoodLog;
import lombok.Data;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FoodLogRepository extends JpaRepository<FoodLog, UUID> {

    List<FoodLog> findByUserIdAndDate(UUID userId, LocalDate date);

}
