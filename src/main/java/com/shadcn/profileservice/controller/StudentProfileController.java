package com.shadcn.profileservice.controller;

import static com.shadcn.profileservice.constant.PathConstant.API_V1;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.shadcn.profileservice.dto.request.StudentFilterRequest;
import com.shadcn.profileservice.dto.request.StudentProfileCreationRequest;
import com.shadcn.profileservice.dto.request.UpdateStudentProfileRequest;
import com.shadcn.profileservice.dto.response.ApiResponse;
import com.shadcn.profileservice.dto.response.PageResponse;
import com.shadcn.profileservice.dto.response.StudentProfileResponse;
import com.shadcn.profileservice.enums.Gender;
import com.shadcn.profileservice.enums.Present;
import com.shadcn.profileservice.service.IStudentProfileService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping(API_V1)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class StudentProfileController {
    IStudentProfileService studentProfileService;

    @PostMapping(value = "/users/student")
    ApiResponse<Void> createStudentProfile(@ModelAttribute StudentProfileCreationRequest request) {
        studentProfileService.createStudentProfile(request);
        return ApiResponse.empty();
    }

    @GetMapping("/users/student/{username}")
    ApiResponse<StudentProfileResponse> getStudentProfileByUsername(@PathVariable String username) {
        return ApiResponse.success(studentProfileService.getStudentProfileByUsername(username));
    }

    @PutMapping("/users/student/{studentId}")
    @PreAuthorize("hasRole('STUDENT') or hasRole('ADMIN')")
    ApiResponse<Void> updateStudentProfile(
            @PathVariable Long studentId, @RequestBody UpdateStudentProfileRequest request) {
        studentProfileService.updateStudentProfile(studentId, request);
        return ApiResponse.empty();
    }

    @GetMapping("/users/students")
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<PageResponse<StudentProfileResponse>> getAllStudentProfiles(
            @RequestParam(required = false) String studentId,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) String dateOfBirth, // Format: dd-MM-yyyy
            @RequestParam(required = false) String phoneNumber,
            @RequestParam(required = false) Gender gender,
            @RequestParam(required = false) Double gpa,
            @RequestParam(required = false) String enrollmentDate, // Format: dd-MM-yyyy
            @RequestParam(required = false) String departmentId,
            @RequestParam(required = false) String guardianName,
            @RequestParam(required = false) String guardianPhoneNumber,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String nationality,
            @RequestParam(required = false) String religion,
            @RequestParam(required = false) String degreeLevel,
            @RequestParam(required = false) String academicYearId,
            @RequestParam(required = false) Present present,
            @RequestParam(required = false) String avatarPath,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortDirection,
            @RequestParam(defaultValue = "1", required = false) Integer current,
            @RequestParam(defaultValue = "10", required = false) Integer pageSize) {

        StudentFilterRequest filterRequest = StudentFilterRequest.builder()
                .studentId(studentId)
                .username(username)
                .firstName(firstName)
                .lastName(lastName)
                .address(address)
                .dateOfBirth(
                        dateOfBirth != null
                                ? LocalDate.parse(dateOfBirth, DateTimeFormatter.ofPattern("dd-MM-yyyy"))
                                : null)
                .phoneNumber(phoneNumber)
                .gender(gender)
                .gpa(gpa != null ? gpa : 0.0) // Default GPA to 0.0 if not provided
                .enrollmentDate(
                        enrollmentDate != null
                                ? LocalDate.parse(enrollmentDate, DateTimeFormatter.ofPattern("dd-MM-yyyy"))
                                : null)
                .departmentId(departmentId)
                .guardianName(guardianName)
                .guardianPhoneNumber(guardianPhoneNumber)
                .email(email)
                .nationality(nationality)
                .religion(religion)
                .degreeLevel(degreeLevel)
                .academicYearId(academicYearId)
                .present(present)
                .avatarPath(avatarPath)
                .sortBy(sortBy != null ? sortBy : "id") // Default sortBy to "id"
                .sortDirection(sortDirection != null ? sortDirection : "asc") // Default sortDirection to "asc"
                .build();

        return ApiResponse.success(studentProfileService.getAllStudentProfiles(filterRequest, current, pageSize));
    }

    @GetMapping("/users/students/ids")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    public ApiResponse<List<StudentProfileResponse>> getAllStudentProfilesByIds(@RequestParam long[] studentIds) {
        return ApiResponse.success(studentProfileService.getAllStudentProfilesByIds(studentIds));
    }

    @GetMapping("/users/students/{studentId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    public ApiResponse<StudentProfileResponse> getStudentProfileById(@PathVariable Long studentId) {
        return ApiResponse.success(studentProfileService.getStudentProfileById(studentId));
    }

    //    @GetMapping("/users/students/usernames")
    //    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    //    public ApiResponse<List<StudentProfileResponse>> getAllStudentProfilesByUsernames(@RequestParam String[]
    // usernames) {
    //        return ApiResponse.success(studentProfileService.getAllStudentProfilesByUsernames(usernames));
    //    }

}
