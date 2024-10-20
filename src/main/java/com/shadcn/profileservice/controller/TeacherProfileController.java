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
public class TeacherProfileController {
    ITeacherProfileService userProfileService;

    @PostMapping("/users/teacher")
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    ApiResponse<Void> createTeacherProfile(@RequestBody TeacherProfileCreationRequest request) {
        userProfileService.createTeacherProfile(request);
        return ApiResponse.empty();
    }

    @GetMapping("/users/teacher/public/{username}")
    ApiResponse<TeacherProfileResponse> getPublicTeacherProfile(@PathVariable String username) {
        return ApiResponse.success(userProfileService.getPublicTeacherProfile(username));
    }

    @GetMapping("/users/teacher/{username}")
    ApiResponse<TeacherProfileResponse> getTeacherProfileByUsername(@PathVariable String username) {
        return ApiResponse.success(userProfileService.getTeacherProfileByUsername(username));
    }

    @PutMapping("/users/teacher/{profileId}")
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    ApiResponse<Void> updateTeacherProfile(
            @PathVariable String profileId, @RequestBody UpdateTeacherProfileRequest request) {
        userProfileService.updateTeacherProfile(profileId, request);
        return ApiResponse.empty();
    }
}
