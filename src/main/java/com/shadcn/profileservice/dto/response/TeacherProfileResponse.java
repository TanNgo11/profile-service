package com.shadcn.profileservice.dto.response;

import java.io.Serializable;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.shadcn.profileservice.enums.Gender;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TeacherProfileResponse implements Serializable {
    String teacherId;
    String id;
    String firstName;
    String lastName;
    @JsonFormat(pattern = "dd-MM-yyyy")
    LocalDate dateOfBirth;

    String city;
    String phoneNumber;
    Gender gender;
    String address;
    @JsonFormat(pattern = "dd-MM-yyyy")
    LocalDate hireDate;

    String department;

    String major;

    Double salary;

    String officeHours;
}
