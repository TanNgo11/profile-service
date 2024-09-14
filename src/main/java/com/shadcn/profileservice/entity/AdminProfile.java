package com.shadcn.profileservice.entity;

import java.io.*;
import java.time.*;

import jakarta.persistence.*;

import com.fasterxml.jackson.annotation.*;
import com.shadcn.profileservice.enums.*;

import lombok.*;
import lombok.experimental.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "admin_profile")
public class AdminProfile extends BaseEntity implements Serializable {
    @Column(unique = true)
    String adminId;

    LocalDate hireDate;

    String department;

    String major;

    String workSchedule;

    String address;

    String emergencyContactName;

    String emergencyContactPhoneNumber;

    String firstName;

    String lastName;

    LocalDate dateOfBirth;

    @Column(unique = true)
    String phoneNumber;

    @Enumerated(EnumType.STRING)
    Gender gender;

    @Column(unique = true)
    String email;

    String avatar;
}
