package com.shadcn.profileservice.dto.response;

import java.time.LocalDate;

import com.shadcn.profileservice.enums.Gender;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserProfileResponse {
    String userId;
    String id;
    String firstName;
    String lastName;
    LocalDate dob;
    String city;
    String phoneNumber;
    Gender gender;
    String address;
}
