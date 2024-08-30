package com.shadcn.profileservice.entity;

import java.time.LocalDate;

import com.shadcn.profileservice.enums.Gender;
import jakarta.persistence.*;
import org.glassfish.jaxb.runtime.v2.runtime.property.Property;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Document(collection = "user_profiles")
public class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    String id;

    String userId;

    String firstName;
    String lastName;
    LocalDate dob;
    String phoneNumber;
    @Enumerated(EnumType.STRING)
    Gender gender;
    String address;
    String email;
    //String avatar;
}
