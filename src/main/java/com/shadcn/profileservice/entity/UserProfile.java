package com.shadcn.profileservice.entity;

import java.io.Serializable;
import java.time.LocalDate;

import com.shadcn.profileservice.enums.Gender;
import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "user_profile")
public class UserProfile implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String userId;

    String firstName;
    String lastName;
    LocalDate dob;
    String phoneNumber;
    @Enumerated(EnumType.STRING)
    Gender gender;
    String address;
    String email;
    String avatar;
}
