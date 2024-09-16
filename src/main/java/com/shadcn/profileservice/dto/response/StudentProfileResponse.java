package com.shadcn.profileservice.dto.response;

import java.io.Serializable;
import java.time.LocalDate;

import com.fasterxml.jackson.databind.annotation.*;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.shadcn.profileservice.enums.Gender;
import com.shadcn.profileservice.util.*;

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
    @JsonDeserialize(using = LocalDateDeserializer.class)
    LocalDate dateOfBirth;

    String city;
    String phoneNumber;
    Gender gender;
    String address;
}
