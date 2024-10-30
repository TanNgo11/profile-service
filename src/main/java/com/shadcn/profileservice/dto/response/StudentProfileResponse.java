package com.shadcn.profileservice.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.shadcn.profileservice.enums.Gender;
import com.shadcn.profileservice.enums.Present;
import com.shadcn.profileservice.validator.DobConstraint;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StudentProfileResponse extends BaseDTOResponse implements Serializable {
    String studentId;

    String username;

    String courseId;

    String firstName;

    String lastName;

    String address;

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
