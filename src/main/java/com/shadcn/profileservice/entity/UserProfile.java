package com.shadcn.profileservice.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;


import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Document(collection = "user_profiles")
public class UserProfile {
    @MongoId
    String id;

    String userId;

    String firstName;
    String lastName;
    LocalDate dateOfBirth;
    String phoneNumber;
    //    @Enumerated(EnumType.STRING)
//    Gender gender;
    String address;
    String email;
    String avatar;

}
