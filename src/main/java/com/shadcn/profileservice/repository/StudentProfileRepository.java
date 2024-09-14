package com.shadcn.profileservice.repository;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shadcn.profileservice.entity.StudentProfile;

@Repository
public interface StudentProfileRepository extends JpaRepository<StudentProfile, String> {
    StudentProfile findTopByOrderByIdDesc();

    Optional<StudentProfile> findByStudentId(String userId);
}
