package com.shadcn.profileservice.controller;

import com.shadcn.profileservice.dto.request.*;
import com.shadcn.profileservice.dto.response.*;
import com.shadcn.profileservice.service.*;
import lombok.*;
import lombok.experimental.*;
import org.springframework.security.access.prepost.*;
import org.springframework.web.bind.annotation.*;

import static com.shadcn.profileservice.constant.PathConstant.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(API_V1)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TeacherProfileController {
    ITeacherProfileService userProfileService;

    @PostMapping("/users/teacher")
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    ApiResponse<Void> createTeacherProfile(@RequestBody TeacherProfileCreationRequest request) {
        userProfileService.createTeacherProfile(request);
        return ApiResponse.empty();
    }

    @GetMapping("/users/teacher/{profileId}")
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    ApiResponse<TeacherProfileResponse> getTeacherProfile(@PathVariable String profileId) {
        return ApiResponse.success(userProfileService.getTeacherProfile(profileId));
    }

    @GetMapping("/users/teacher")
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<PageResponse<TeacherProfileResponse>> getAllTeacherProfiles(
            @RequestParam(defaultValue = "1", required = false) Integer current,
            @RequestParam(defaultValue = "10", required = false) Integer pageSize) {
        return ApiResponse.success(userProfileService.getAllTeacherProfiles(current, pageSize));
    }

    @PutMapping("/users/teacher/{profileId}")
    @PreAuthorize("hasRole('TEACHER')or hasRole('ADMIN')")
    ApiResponse<Void> updateTeacherProfile(
            @PathVariable String profileId, @RequestBody UpdateTeacherProfileRequest request) {
        userProfileService.updateTeacherProfile(profileId, request);
        return ApiResponse.empty();
    }
}
