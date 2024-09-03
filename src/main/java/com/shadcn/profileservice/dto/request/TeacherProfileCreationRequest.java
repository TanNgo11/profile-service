package com.shadcn.profileservice.dto.request;

import com.shadcn.profileservice.enums.Gender;
import com.shadcn.profileservice.validator.DobConstraint;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TeacherProfileCreationRequest {
    String userId;
    String firstName;
    String lastName;
    String email;
    String phoneNumber;
    Gender gender;
    String address;

    @DobConstraint(min = 18, message = "INVALID_DOB")
    LocalDate dateOfBirth;


}
