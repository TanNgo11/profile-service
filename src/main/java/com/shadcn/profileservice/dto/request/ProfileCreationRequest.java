package com.shadcn.profileservice.dto.request;

import java.time.LocalDate;

import com.shadcn.profileservice.enums.Status;
import com.shadcn.profileservice.validator.DobConstraint;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProfileCreationRequest {
    String userId;
    String firstName;
    String lastName;
    String email;
    String phoneNumber;
    String gender;
    String address;

    @DobConstraint(min = 18, message = "INVALID_DOB")
    LocalDate dob;
}
