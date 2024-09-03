package com.shadcn.profileservice.entity;

import com.shadcn.profileservice.enums.Gender;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "user_profile")
public class AdminProfile implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String userId;

    String firstName;
    String lastName;
    LocalDate dateOfBirth;
    String phoneNumber;
    @Enumerated(EnumType.STRING)
    Gender gender;
    String address;
    String email;
    String avatar;
}
