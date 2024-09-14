package com.shadcn.profileservice.dto.request;

import java.time.*;

import jakarta.persistence.*;

import com.fasterxml.jackson.annotation.*;
import com.shadcn.profileservice.enums.*;
import com.shadcn.profileservice.validator.*;

import lombok.*;
import lombok.experimental.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BaseUpdateProfile {

    String firstName;
    String lastName;
    String email;
    String phoneNumber;
    String address;

    @DobConstraint(min = 18, message = "INVALID_DOB")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    Gender gender;

    String avatar;
}
