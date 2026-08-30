package com.foodcal.foodcal_backend.service;

import com.foodcal.foodcal_backend.entity.UserDetail;
import com.foodcal.foodcal_backend.exception.InvalidRequestException;
import com.foodcal.foodcal_backend.repository.UserDetailRepository;
import org.springframework.stereotype.Service;

@Service
public class UserDetailService {

    private final UserDetailRepository userDetailRepository;

    public UserDetailService(UserDetailRepository userDetailRepository){
        this.userDetailRepository = userDetailRepository;
    }

    public UserDetail updateUser(UserDetail user) {

        UserDetail userDetailDB = userDetailRepository.findById(user.getId())
                .orElseThrow(() -> new InvalidRequestException("User not found"));

        if (isNotBlank(user.getUserName())) {
            userDetailDB.setUserName(user.getUserName());
        }

        if (isNotBlank(user.getFullName())) {
            userDetailDB.setFullName(user.getFullName());
        }

        if (isNotBlank(user.getEmail())) {
            userDetailDB.setEmail(user.getEmail());
        }

        if (isNotBlank(user.getGender())) {
            userDetailDB.setGender(user.getGender());
        }

        if (isNotBlank(user.getAvatarUrl())) {
            userDetailDB.setAvatarUrl(user.getAvatarUrl());
        }

        return userDetailRepository.save(userDetailDB);
    }

    private static boolean isNotBlank(String value){
        return value != null && !value.trim().isEmpty();
    }
}
