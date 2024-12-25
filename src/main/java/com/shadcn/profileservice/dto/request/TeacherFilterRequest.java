package com.shadcn.profileservice.dto.request;

import java.time.LocalDate;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.shadcn.profileservice.enums.Gender;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TeacherFilterRequest {
    String teacherId;

    String username;

    @JsonFormat(pattern = "dd-MM-yyyy")
    LocalDate hireDate;

    String departmentId;

    Double salary;

    String officeHours;

    String address;

    String emergencyContactName;

    String emergencyContactPhoneNumber;

    String fullName;

    @JsonFormat(pattern = "dd-MM-yyyy")
    LocalDate dateOfBirth;

    String phoneNumber;

    @Enumerated(EnumType.STRING)
    Gender gender;

    String email;

    String avatarPath;
    String sortBy;
    String sortDirection;
}
