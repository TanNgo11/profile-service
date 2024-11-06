package com.shadcn.profileservice.dto.response;

import java.io.Serializable;
import java.time.LocalDate;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.shadcn.profileservice.enums.Gender;
import com.shadcn.profileservice.enums.Present;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StudentProfileResponse extends BaseDTOResponse implements Serializable {
    String studentId;

    String username;

    String firstName;

    String lastName;

    String address;

    @JsonFormat(pattern = "dd-MM-yyyy")
    LocalDate dateOfBirth;

    String phoneNumber;

    @Enumerated(EnumType.STRING)
    Gender gender;

    double gpa;

    @JsonFormat(pattern = "dd-MM-yyyy")
    LocalDate enrollmentDate;

    String departmentId;

    String guardianName;

    String guardianPhoneNumber;

    String email;

    String nationality;

    String religion;

    String degreeLevel;

    String academicYearId;

    @Enumerated(EnumType.STRING)
    Present present;

    String avatarPath;
}
