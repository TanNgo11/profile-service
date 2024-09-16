package com.shadcn.profileservice.entity;

import java.io.*;
import java.time.*;

import jakarta.persistence.*;

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

    String grade;

    LocalDate enrollmentDate;

    String major;

    String advisorId;

    String guardianName;

    String guardianPhoneNumber;

    String address;

    String firstName;

    String lastName;

    @DobConstraint(min = 10, message = "INVALID_DOB")
    LocalDate dateOfBirth;

    @Column(unique = true)
    String phoneNumber;

    @Enumerated(EnumType.STRING)
    Gender gender;

    @Column(unique = true)
    String email;

    String avatar;
}
