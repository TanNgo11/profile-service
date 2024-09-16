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

    @PostMapping("/users/teacher/profile")
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    ApiResponse<Void> createTeacherProfile(@RequestBody TeacherProfileCreationRequest request) {
        userProfileService.createTeacherProfile(request);
        return ApiResponse.empty();
    }

    @GetMapping("/users/teacher/profile/{profileId}")
    ApiResponse<TeacherProfileResponse> getPublicTeacherProfile(@PathVariable String profileId) {
        return ApiResponse.success(userProfileService.getTeacherProfileById(profileId));
    }

    @PutMapping("/users/teacher/profile/{profileId}")
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    ApiResponse<Void> updateTeacherProfile(
            @PathVariable String profileId, @RequestBody UpdateTeacherProfileRequest request) {
        userProfileService.updateTeacherProfile(profileId, request);
        return ApiResponse.empty();
    }
}
