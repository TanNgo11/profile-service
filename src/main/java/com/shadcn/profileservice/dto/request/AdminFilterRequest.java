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
public class AdminFilterRequest {
    String fullName;
    String email;
    String phoneNumber;
    String username;

    @Enumerated(EnumType.STRING)
    Gender gender;

    String address;

    @JsonFormat(pattern = "dd-MM-yyyy")
    LocalDate hireDate;

    String departmentId;
    String workSchedule;
    String emergencyContactName;
    String emergencyContactPhoneNumber;
    String sortDirection;
    String sortBy;
}
