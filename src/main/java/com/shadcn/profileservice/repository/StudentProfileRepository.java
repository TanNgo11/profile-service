package com.shadcn.profileservice.repository;

import java.util.*;

import com.shadcn.profileservice.dto.response.StudentProfileResponse;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.shadcn.profileservice.entity.StudentProfile;

@Repository
public interface StudentProfileRepository extends JpaRepository<StudentProfile, Long> {
    StudentProfile findTopByOrderByIdDesc();

    Optional<StudentProfile> findByUsername(String username);

    boolean existsByPhoneNumber(String phone);

    List<StudentProfile> findAllByUsernameIn(List<String> usernames);

    List<StudentProfile> findAllByAcademicYearId(Long academicYearId);

    @Query("SELECT s FROM StudentProfile s WHERE s.studentId = :studentId")
    StudentProfile getStudentProfileByStudentId(@Param("studentId") long studentId);
}
