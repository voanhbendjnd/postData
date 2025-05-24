package com.djnd.post_data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.djnd.post_data.domain.entity.UserOTP;

@Repository
public interface UserOTPRepository extends JpaRepository<UserOTP, Long> {
    boolean existsByCode(int code);
}
