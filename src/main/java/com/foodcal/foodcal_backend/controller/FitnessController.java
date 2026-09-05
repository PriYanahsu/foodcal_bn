package com.foodcal.foodcal_backend.controller;

import com.foodcal.foodcal_backend.entity.FitnessDetail;
import com.foodcal.foodcal_backend.security.UserPrincipal;
import com.foodcal.foodcal_backend.service.FitnessService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("/api/v1/fitness")
public class FitnessController {

    private final FitnessService fitnessService;

    public FitnessController (
            FitnessService fitnessService
    ){
        this.fitnessService = fitnessService;
    }

    @GetMapping("/get")
    public ResponseEntity<FitnessDetail> getFitnessDetails(
            Authentication authentication
    ){
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        UUID userID = userPrincipal.getId();

        return ResponseEntity.status(HttpStatus.OK)
                .body(fitnessService.getFitnessDetail(userID));
    }

    @PutMapping("/update")
    public ResponseEntity<FitnessDetail> updateFitnessDetails(
            @RequestBody FitnessDetail fitnessDetail,
            Authentication authentication
    ){
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        UUID userID = userPrincipal.getId();

        return ResponseEntity.status(HttpStatus.OK)
                .body(fitnessService.updateFitnessDetail(userID, fitnessDetail));
    }
}
