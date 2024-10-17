package com.shadcn.profileservice.repository;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shadcn.profileservice.entity.AdminProfile;

@Repository
public interface AdminProfileRepository extends JpaRepository<AdminProfile, String> {

    AdminProfile findTopByOrderByIdDesc();

    Optional<AdminProfile> findByAdminId(String adminId);

    Optional<AdminProfile> findByUsername(String username);

    boolean existsByPhoneNumber(String phone);
}
