package com.shadcn.profileservice.dto.request;

import java.time.LocalDate;

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
public class StudentProfileCreationRequest {
    String firstName;
    String lastName;
    String email;
    String phoneNumber;
    String username;
    Gender gender;
    String address;

    @JsonFormat(pattern = "dd-MM-yyyy")
    LocalDate dateOfBirth;

    String avatar;
}
