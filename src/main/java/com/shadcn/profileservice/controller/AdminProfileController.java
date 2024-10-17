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
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<Void> createAdminProfile(@ModelAttribute AdminProfileCreationRequest request) {
        adminProfileService.createAdminProfile(request);
        return ApiResponse.empty();
    }

    @GetMapping("/users/admin/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    AdminProfileResponse getAdminProfileByUsername(@PathVariable String username) {
        return adminProfileService.getAdminProfileByUsername(username);
    }

    @GetMapping("/users/admin")
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<PageResponse<AdminProfileResponse>> getAllAdminProfiles(
            @RequestParam(defaultValue = "1", required = false) Integer current,
            @RequestParam(defaultValue = "10", required = false) Integer pageSize) {
        return ApiResponse.success(adminProfileService.getAllAdminProfiles(current, pageSize));
    }

    @PutMapping("/users/admin/{profileId}")
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<Void> updateAdminProfile(
            @PathVariable String profileId, @RequestBody UpdateAdminProfileRequest request) {
        adminProfileService.updateAdminProfile(profileId, request);
        return ApiResponse.empty();
    }


}
