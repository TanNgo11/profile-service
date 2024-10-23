package com.shadcn.profileservice.dto.response;

import java.io.Serializable;
import java.time.LocalDate;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.shadcn.profileservice.entity.*;
import com.shadcn.profileservice.enums.Gender;
import com.shadcn.profileservice.util.*;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StudentProfileResponse implements Serializable {
    String studentId;
    String id;
    String firstName;
    String lastName;

    @JsonFormat(pattern = "dd-MM-yyyy")
    LocalDate dateOfBirth;

    String city;
    String phoneNumber;

    @Enumerated(EnumType.STRING)
    Gender gender;

    String address;
    String grade;

    @JsonFormat(pattern = "dd-MM-yyyy")
    LocalDate enrollmentDate;

    String major;

    String guardianName;

    String guardianPhoneNumber;

    String email;

    String avatarPath;
    // 54 dân tộc :)))
    String nation;

    String religion;

    String citizenId;
    // At the moment just have Information technology and Business Administration
    String faculty;
    // Ex: Đại học chính quy Tiếng Việt K10
    String degreeLevel;

    String schoolYear;
}
