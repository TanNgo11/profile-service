package com.shadcn.profileservice.repository;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shadcn.profileservice.entity.StudentProfile;

@Repository
public interface StudentProfileRepository extends JpaRepository<StudentProfile, Long> {
    StudentProfile findTopByOrderByIdDesc();

    Optional<StudentProfile> findByUsername(String username);

    boolean existsByPhoneNumber(String phone);
}
