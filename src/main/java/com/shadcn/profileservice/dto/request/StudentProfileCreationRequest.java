package com.shadcn.profileservice.dto.request;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.*;
import com.shadcn.profileservice.entity.*;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StudentProfileCreationRequest {
    String username;

    String email;

    String firstName;

    String middleName;

    String lastName;

    String address;

    @JsonFormat(pattern = "dd-MM-yyyy")
    LocalDate dateOfBirth;

    String phoneNumber;

    String gender;

    @JsonFormat(pattern = "dd-MM-yyyy")
    LocalDate enrollmentDate;

    Long departmentId;

    String guardianName;

    String guardianPhoneNumber;

    String nationality;

    String religion;

    String degreeLevel;

    String academicYearId;

    String avatar;

    String citizenId;
}
