package com.shadcn.profileservice.dto.response;

import java.io.Serializable;
import java.time.LocalDate;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.shadcn.profileservice.entity.*;
import com.shadcn.profileservice.enums.Gender;

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

    @JsonSerialize(using = LocalDateSerializer.class)
    LocalDate dateOfBirth;

    String city;
    String phoneNumber;
    Gender gender;
    String address;
}
