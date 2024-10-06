package com.shadcn.profileservice.controller;

import static com.shadcn.profileservice.constant.PathConstant.*;

import org.springframework.security.access.prepost.*;
import org.springframework.web.bind.annotation.*;

import com.shadcn.profileservice.dto.request.*;
import com.shadcn.profileservice.dto.response.*;
import com.shadcn.profileservice.service.*;

import lombok.*;
import lombok.experimental.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(API_V1)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@PreAuthorize("hasRole('ADMIN')")
public class AdminProfileController {
    IAdminProfileService adminProfileService;
    IStudentProfileService studentProfileService;
    ITeacherProfileService teacherProfileService;

    @PostMapping("/users/admin")
    ApiResponse<Void> createAdminProfile(@RequestBody AdminProfileCreationRequest request) {
        adminProfileService.createAdminProfile(request);
        return ApiResponse.empty();
    }

    @GetMapping("/users/admin/{profileId}")
    AdminProfileResponse getAdminProfile(@PathVariable String profileId) {
        return adminProfileService.getAdminProfile(profileId);
    }

    @GetMapping("/users/admin")
    ApiResponse<PageResponse<AdminProfileResponse>> getAllAdminProfiles(
            @RequestParam(defaultValue = "1", required = false) Integer current,
            @RequestParam(defaultValue = "10", required = false) Integer pageSize) {
        return ApiResponse.success(adminProfileService.getAllAdminProfiles(current, pageSize));
    }

    @PutMapping("/users/admin/{profileId}")
    ApiResponse<Void> updateAdminProfile(
            @PathVariable String profileId, @RequestBody UpdateAdminProfileRequest request) {
        adminProfileService.updateAdminProfile(profileId, request);
        return ApiResponse.empty();
    }

    @GetMapping("/users/student")
    ApiResponse<PageResponse<StudentProfileResponse>> getAllStudentProfiles(
            @RequestParam(defaultValue = "1", required = false) Integer current,
            @RequestParam(defaultValue = "10", required = false) Integer pageSize) {
        return ApiResponse.success(studentProfileService.getAllStudentProfiles(current, pageSize));
    }

    @GetMapping("/users/teacher")
    ApiResponse<PageResponse<TeacherProfileResponse>> getAllTeacherProfiles(
            @RequestParam(defaultValue = "1", required = false) Integer current,
            @RequestParam(defaultValue = "10", required = false) Integer pageSize) {
        return ApiResponse.success(teacherProfileService.getAllTeacherProfiles(current, pageSize));
    }

    @GetMapping("/users/teacher/{profileId}")
    ApiResponse<TeacherProfileResponse> getPrivateTeacherProfile(@PathVariable String profileId) {
        return ApiResponse.success(teacherProfileService.getTeacherProfileById(profileId));
    }
}
