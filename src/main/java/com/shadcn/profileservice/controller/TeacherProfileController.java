package com.shadcn.profileservice.controller;

import static com.shadcn.profileservice.constant.PathConstant.*;

import org.springframework.security.access.prepost.*;
import org.springframework.web.bind.annotation.*;

import com.shadcn.profileservice.dto.request.*;
import com.shadcn.profileservice.dto.response.*;
import com.shadcn.profileservice.service.*;

import lombok.*;
import lombok.experimental.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(API_V1)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TeacherProfileController {
    ITeacherProfileService teacherProfileService;

    @PostMapping("/users/teacher")
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    ApiResponse<Void> createTeacherProfile(@ModelAttribute TeacherProfileCreationRequest request) {
        teacherProfileService.createTeacherProfile(request);
        return ApiResponse.empty();
    }

    @GetMapping("/users/teacher/public/{username}")
    ApiResponse<TeacherProfileResponse> getPublicTeacherProfile(@PathVariable String username) {
        return ApiResponse.success(teacherProfileService.getPublicTeacherProfile(username));
    }

    @GetMapping("/users/teacher/{username}")
    ApiResponse<TeacherProfileResponse> getTeacherProfileByUsername(@PathVariable String username) {
        return ApiResponse.success(teacherProfileService.getTeacherProfileByUsername(username));
    }

    @PutMapping("/users/teacher/{profileId}")
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    ApiResponse<Void> updateTeacherProfile(
            @PathVariable String profileId, @RequestBody UpdateTeacherProfileRequest request) {
        teacherProfileService.updateTeacherProfile(profileId, request);
        return ApiResponse.empty();
    }
    @GetMapping("/users/teachers")
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<PageResponse<TeacherProfileResponse>> getAllTeacherProfiles(
            @RequestParam(defaultValue = "1", required = false) Integer current,
            @RequestParam(defaultValue = "10", required = false) Integer pageSize) {
        return ApiResponse.success(teacherProfileService.getAllTeacherProfiles(current, pageSize));
    }
    @GetMapping("/users/teachers/ids")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<List<TeacherProfileResponse>> getAllTeacherProfilesByIds(@RequestParam long[] teacherIds) {
        return ApiResponse.success(teacherProfileService.getAllTeacherProfilesByIds(teacherIds));
    }

    @GetMapping("/users/teachers/usernames")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<List<TeacherProfileResponse>> getAllTeacherProfilesByUsernames(@RequestParam String[] usernames) {
        return ApiResponse.success(teacherProfileService.getAllTeacherProfilesByUsernames(usernames));
    }

}
