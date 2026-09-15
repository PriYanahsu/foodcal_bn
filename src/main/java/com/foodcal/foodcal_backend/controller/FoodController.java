package com.foodcal.foodcal_backend.controller;

import com.foodcal.foodcal_backend.dto.FoodLogStatsDateResponse;
import com.foodcal.foodcal_backend.entity.FoodLog;
import com.foodcal.foodcal_backend.security.UserPrincipal;
import com.foodcal.foodcal_backend.service.FoodService;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/food")
public class FoodController {

    public FoodService foodService;

    public FoodController(
            FoodService foodService
    ){
        this.foodService = foodService;
    }


    @PostMapping(
            value = "/{userID}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<FoodLog> uploadFoodLog(
            @PathVariable UUID userID,
            @RequestParam("file") MultipartFile file,
            @RequestPart("foodData") FoodLog foodData,
            Authentication authentication
    ) {
        UserPrincipal userPrincipal =
                (UserPrincipal) authentication.getPrincipal();

        if (!userPrincipal.getId().equals(userID)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.ok(
                foodService.addFoodService(userID, file, foodData)
        );
    }

    @GetMapping("/stats/{date}")
    public ResponseEntity<FoodLogStatsDateResponse> getStatsFoodLog(
            @PathVariable LocalDate date,
            Authentication authentication
    ) {
        UserPrincipal userPrincipal =
                (UserPrincipal) authentication.getPrincipal();

        return ResponseEntity.ok(
                foodService.getStatsFoodLog(userPrincipal.getId(), date)
        );
    }

    @GetMapping("/logs/{date}")
    public ResponseEntity<List<FoodLog>> getFoodLogDateResponse(
            @PathVariable LocalDate date,
            Authentication authentication
    ){
        UserPrincipal userPrincipal =
                (UserPrincipal) authentication.getPrincipal();

        return ResponseEntity.ok(
                foodService.getFoodLog(userPrincipal.getId(), date)
        );
    }
}
