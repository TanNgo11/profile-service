package com.shadcn.profileservice.repository;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shadcn.profileservice.entity.TeacherProfile;

@Repository
public interface TeacherProfileRepository extends JpaRepository<TeacherProfile, String> {
    TeacherProfile findTopByOrderByIdDesc();

    Optional<TeacherProfile> findByTeacherId(String teacherId);

    Optional<TeacherProfile> findByUsername(String username);

    boolean existsByPhoneNumber(String phone);
}
