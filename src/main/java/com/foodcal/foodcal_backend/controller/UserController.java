package com.foodcal.foodcal_backend.controller;

import com.foodcal.foodcal_backend.entity.FitnessDetail;
import com.foodcal.foodcal_backend.entity.UserDetail;
import com.foodcal.foodcal_backend.security.UserPrincipal;
import com.foodcal.foodcal_backend.service.FitnessService;
import com.foodcal.foodcal_backend.service.UserDetailService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@Controller
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserDetailService userDetailService;
    private final FitnessService fitnessService;

    public UserController(UserDetailService userDetailService, FitnessService fitnessService){
        this.userDetailService = userDetailService;
        this.fitnessService = fitnessService;
    }

    @PutMapping("/userDetails/update")
    public ResponseEntity<UserDetail> updateUserDetails(@RequestBody UserDetail user){
        return ResponseEntity.status(HttpStatus.OK).body(userDetailService.updateUser(user));
    }

    @PostMapping("/fitness/add")
    public ResponseEntity<FitnessDetail> addFitnessDetails(
            @RequestBody FitnessDetail fitnessDetails,
            Authentication authentication
    ){
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        UUID userID = userPrincipal.getId();

        return ResponseEntity.status(HttpStatus.OK)
                .body(fitnessService.addFitnessDetail(fitnessDetails, userID));
    }
}
