package com.shadcn.profileservice.dto.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.shadcn.profileservice.enums.Gender;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AdminProfileResponse implements Serializable {
    String userId;
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
