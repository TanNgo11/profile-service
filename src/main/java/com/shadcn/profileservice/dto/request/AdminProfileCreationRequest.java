package com.shadcn.profileservice.dto.request;

import java.time.LocalDate;
import java.util.*;

import jakarta.persistence.*;

import org.springframework.cglib.core.*;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.*;
import com.fasterxml.jackson.datatype.jsr310.deser.*;
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
public class AdminProfileCreationRequest {

    String firstName;
    String lastName;
    String email;
    String phoneNumber;

    @Enumerated(EnumType.STRING)
    Gender gender;

    String address;

    @DobConstraint(min = 18, message = "INVALID_DOB")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    LocalDate dateOfBirth;

    String avatar;
}
