package com.shadcn.profileservice.controller;

import static com.shadcn.profileservice.constant.PathConstant.API_V1;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.shadcn.profileservice.dto.request.StudentProfileCreationRequest;
import com.shadcn.profileservice.dto.request.UpdateStudentProfileRequest;
import com.shadcn.profileservice.dto.response.ApiResponse;
import com.shadcn.profileservice.dto.response.PageResponse;
import com.shadcn.profileservice.dto.response.StudentProfileResponse;
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
            @PathVariable String studentId, @RequestBody UpdateStudentProfileRequest request) {
        studentProfileService.updateStudentProfile(studentId, request);
        return ApiResponse.empty();
    }

    @GetMapping("/users/students")
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<PageResponse<StudentProfileResponse>> getAllStudentProfiles(
            @RequestParam(defaultValue = "1", required = false) Integer current,
            @RequestParam(defaultValue = "10", required = false) Integer pageSize) {
        return ApiResponse.success(studentProfileService.getAllStudentProfiles(current, pageSize));
    }

    @GetMapping("/users/students/ids")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    public ApiResponse<List<StudentProfileResponse>> getAllStudentProfilesByIds(@RequestParam long[] studentIds) {
        return ApiResponse.success(studentProfileService.getAllStudentProfilesByIds(studentIds));
    }

    @GetMapping("/users/students/{studentId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    public ApiResponse<StudentProfileResponse> getAllStudentProfilesById(@PathVariable Long studentId) {
        return ApiResponse.success(studentProfileService.getStudentProfileById(studentId));
    }

    //    @GetMapping("/users/students/usernames")
    //    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    //    public ApiResponse<List<StudentProfileResponse>> getAllStudentProfilesByUsernames(@RequestParam String[]
    // usernames) {
    //        return ApiResponse.success(studentProfileService.getAllStudentProfilesByUsernames(usernames));
    //    }

}
