package com.foodcal.foodcal_backend.controller;

import com.foodcal.foodcal_backend.entity.FitnessDetail;
import com.foodcal.foodcal_backend.entity.UserDetail;
import com.foodcal.foodcal_backend.repository.FitnessDetailRepository;
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

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserDetailService userDetailService;

    public UserController(
            UserDetailService userDetailService,
            FitnessService fitnessService,
            FitnessDetailRepository fitnessDetailRepository
    ){
        this.userDetailService = userDetailService;
    }

    @PutMapping("/update")
    public ResponseEntity<UserDetail> updateUserDetails(@RequestBody UserDetail user){
        return ResponseEntity.status(HttpStatus.OK).body(userDetailService.updateUser(user));
    }
}
