package com.shadcn.profileservice.dto.response;

import java.io.Serializable;
import java.time.LocalDate;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.shadcn.profileservice.enums.Gender;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AdminProfileResponse extends BaseDTOResponse implements Serializable {
    String adminId;

    String username;

    @JsonFormat(pattern = "dd-MM-yyyy")
    LocalDate hireDate;

    String departmentId;

    String workSchedule;

    String address;

    String emergencyContactName;

    String emergencyContactPhoneNumber;

    String firstName;
    
    String middleName;

    String lastName;

    @JsonFormat(pattern = "dd-MM-yyyy")
    LocalDate dateOfBirth;

    String phoneNumber;

    @Enumerated(EnumType.STRING)
    Gender gender;

    String email;

    String avatarPath;
}
