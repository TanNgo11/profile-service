package com.shadcn.profileservice.dto.response;

import java.io.*;
import java.time.*;

import com.fasterxml.jackson.databind.annotation.*;
import com.fasterxml.jackson.datatype.jsr310.ser.*;
import com.shadcn.profileservice.enums.*;

import lombok.*;
import lombok.experimental.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AdminProfileResponse implements Serializable {

    String adminId;
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
