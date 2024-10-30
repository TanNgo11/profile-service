package com.shadcn.profileservice.entity;

import java.io.*;
import java.time.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;

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

    String courseId;

    String firstName;

    String lastName;

    String address;

    @DobConstraint(min = 10, message = "INVALID_DOB")
    @JsonFormat(pattern = "dd-MM-yyyy")
    LocalDate dateOfBirth;

    String phoneNumber;

    @Enumerated(EnumType.STRING)
    Gender gender;

    double gpa;

    LocalDate enrollmentDate;

    String major;

    String guardianName;

    String guardianPhoneNumber;

    String email;

    String nationality;

    // 54 dân tộc :)))
    String nation;

    String religion;

    String citizenId;
    // At the moment just have Information technology and Business Administration
    String faculty;
    // Ex: Đại học chính quy Tiếng Việt K10
    String degreeLevel;

    @Pattern(regexp = "^2\\d{3}-2\\d{3}$", message = "School year must be between 2000-2099")
    String schoolYear;

    @Enumerated(EnumType.STRING)
    Present present;

    @Column(name = "city")
    private String city;

    String avatarPath;

}
