package com.shadcn.profileservice.entity;

import java.io.*;
import java.time.*;

import jakarta.persistence.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.shadcn.profileservice.enums.*;
import com.shadcn.profileservice.validator.*;

import lombok.*;
import lombok.experimental.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "student_profile")
public class StudentProfile extends BaseEntity implements Serializable {
    @Column(unique = true)
    String studentId;

    @Column(unique = true)
    String username;

    String firstName;

    String middleName;

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

    // major-faculty => department
    Long departmentId;

    String guardianName;

    String guardianPhoneNumber;

    String email;

    String nationality;

    String religion;

    // Ex: Đại học chính quy Tiếng Việt K10
    String degreeLevel;

    Long academicYearId;

    @Enumerated(EnumType.STRING)
    Present present;

    String avatarPath;
}
