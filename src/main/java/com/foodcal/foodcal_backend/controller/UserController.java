package com.foodcal.foodcal_backend.controller;

import com.foodcal.foodcal_backend.entity.UserDetail;

import com.foodcal.foodcal_backend.security.UserPrincipal;
import com.foodcal.foodcal_backend.service.UserDetailService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserDetailService userDetailService;

    public UserController(
            UserDetailService userDetailService
    ){
        this.userDetailService = userDetailService;
    }

    @PutMapping("/update")
    public ResponseEntity<UserDetail> updateUserDetails(
            @RequestBody UserDetail user,
            Authentication authentication
    ){
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        UUID userID = userPrincipal.getId();
        return ResponseEntity.status(HttpStatus.OK).body(userDetailService.updateUser(user, userID));
    }

    @GetMapping("/{userID}")
    public ResponseEntity<UserDetail> getUserDetails(@PathVariable UUID userID){
        return ResponseEntity.status(HttpStatus.OK).body(userDetailService.getUserDetails(userID));
    }
}
