package com.foodcal.foodcal_backend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;
import com.foodcal.foodcal_backend.entity.Weight;
import com.foodcal.foodcal_backend.service.WeightService;
import org.springframework.security.core.Authentication;
import com.foodcal.foodcal_backend.security.UserPrincipal;
import java.util.UUID;
import java.util.List;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/v1/weight")
public class WeightController {

    public WeightService weightService;

    public WeightController(WeightService weightService) {
        this.weightService = weightService;
    }

    @PostMapping("/log")
    public ResponseEntity<Weight> logWeight(
        @RequestBody Weight weight,
        Authentication authentication
    ) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        UUID userId = userPrincipal.getId();
        return weightService.logWeight(weight, userId);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<Weight>> getWeights(
        @PathVariable UUID userId,
        Authentication authentication
    ) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        if(!userPrincipal.getId().equals(userId)) {
            throw new RuntimeException("User not authorized to get weights");
        }
        return weightService.getWeights(userId);
    }
}