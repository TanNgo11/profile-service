package com.shadcn.profileservice.dto.request;

import java.time.LocalDate;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.*;
import com.shadcn.profileservice.entity.*;
import com.shadcn.profileservice.enums.Gender;
import com.shadcn.profileservice.validator.DobConstraint;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TeacherProfileCreationRequest {

    String username;

    @JsonFormat(pattern = "dd-MM-yyyy")
    LocalDate hireDate;

    String departmentId;

    Double salary;

    String officeHours;

    String address;

    String emergencyContactName;

    String emergencyContactPhoneNumber;

    String firstName;

    String lastName;

    @DobConstraint(min = 18, message = "INVALID_DOB")
    @JsonFormat(pattern = "dd-MM-yyyy")
    LocalDate dateOfBirth;

    String phoneNumber;

    Gender gender;

    String email;

    MultipartFile avatar;
}
