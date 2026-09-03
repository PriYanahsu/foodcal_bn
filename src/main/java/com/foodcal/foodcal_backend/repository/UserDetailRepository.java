package com.foodcal.foodcal_backend.repository;

import com.foodcal.foodcal_backend.entity.UserDetail;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDetailRepository extends JpaRepository<UserDetail, UUID> {

    boolean existsByEmailIgnoreCase(String email);

    UserDetail findByEmailIgnoreCase(String email);
}
