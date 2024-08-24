package com.shadcn.profileservice.entity;

import java.io.Serializable;
import java.time.LocalDate;

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
    private Long id; //

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
