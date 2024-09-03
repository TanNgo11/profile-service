package com.shadcn.profileservice.controller;

import com.shadcn.profileservice.dto.request.AdminProfileCreationRequest;
import com.shadcn.profileservice.dto.request.StudentProfileCreationRequest;
import com.shadcn.profileservice.dto.request.TeacherProfileCreationRequest;
import com.shadcn.profileservice.dto.response.*;
import com.shadcn.profileservice.service.impl.UserProfileService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.shadcn.profileservice.constant.PathConstant.API_V1;

@RestController
@RequiredArgsConstructor
@RequestMapping(API_V1)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserProfileController {
    UserProfileService userProfileService;

    @PostMapping("/users/student")
    StudentProfileResponse createStudentProfile(@RequestBody StudentProfileCreationRequest request) {
        return userProfileService.createStudentProfile(request);
    }

    @PostMapping("/users/teacher")
    TeacherProfileResponse createTeacherProfile(@RequestBody TeacherProfileCreationRequest request) {
        return userProfileService.createTeacherProfile(request);
    }

    @PostMapping("/users/admin")
    AdminProfileResponse createAdminProfile(@RequestBody AdminProfileCreationRequest request) {
        return userProfileService.createAdminProfile(request);
    }

    @GetMapping("/users/student/{profileId}")
    StudentProfileResponse getStudentProfile(@PathVariable String profileId) {
        return userProfileService.getStudentProfile(profileId);
    }

    @GetMapping("/users/teacher/{profileId}")
    TeacherProfileResponse getTeacherProfile(@PathVariable String profileId) {
        return userProfileService.getTeacherProfile(profileId);
    }

    @GetMapping("/users/admin/{profileId}")
    AdminProfileResponse getAdminProfile(@PathVariable String profileId) {
        return userProfileService.getAdminProfile(profileId);
    }

    @GetMapping("/users/student")
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<PageResponse<StudentProfileResponse>> getAllProfiles(
            @RequestParam(defaultValue = "1", required = false) Integer current,
            @RequestParam(defaultValue = "10", required = false) Integer pageSize) {
        return ApiResponse.success(userProfileService.getAllProfiles(current, pageSize));
    }

    @GetMapping("/users/teacher")
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<PageResponse<TeacherProfileResponse>> getAllProfiles(
            @RequestParam(defaultValue = "1", required = false) Integer current,
            @RequestParam(defaultValue = "10", required = false) Integer pageSize) {
        return ApiResponse.success(userProfileService.getAllProfiles(current, pageSize));
    }

    @GetMapping("/users/admin")
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<PageResponse<AdminProfileResponse>> getAllProfiles(
            @RequestParam(defaultValue = "1", required = false) Integer current,
            @RequestParam(defaultValue = "10", required = false) Integer pageSize) {
        return ApiResponse.success(userProfileService.getAllProfiles(current, pageSize));
    }
}
