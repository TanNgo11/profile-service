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
public class StudentProfileController {
    IStudentProfileService userProfileService;

    @PostMapping("/users/student")
    ApiResponse<Void> createStudentProfile(@RequestBody StudentProfileCreationRequest request) {
        userProfileService.createStudentProfile(request);
        return ApiResponse.empty();
    }

    @GetMapping("/users/student/{profileId}")
    ApiResponse<StudentProfileResponse> getStudentProfile(@PathVariable String profileId) {
        return ApiResponse.success(userProfileService.getStudentProfile(profileId));
    }

    @GetMapping("/users/student")
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<PageResponse<StudentProfileResponse>> getAllStudentProfiles(
            @RequestParam(defaultValue = "1", required = false) Integer current,
            @RequestParam(defaultValue = "10", required = false) Integer pageSize) {
        return ApiResponse.success(userProfileService.getAllStudentProfiles(current, pageSize));
    }

    @PutMapping("/users/student/{profileId}")
    @PreAuthorize("hasRole('STUDENT')or hasRole('ADMIN')")
    ApiResponse<Void> updateStudentProfile(
            @PathVariable String profileId, @RequestBody UpdateStudentProfileRequest request) {
        userProfileService.updatStudentProfile(profileId, request);
        return ApiResponse.empty();
    }
}
