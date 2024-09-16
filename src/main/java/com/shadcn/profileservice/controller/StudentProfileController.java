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
public class StudentProfileController {
    IStudentProfileService userProfileService;

    @PostMapping("/users/student/profile")
    ApiResponse<Void> createStudentProfile(@RequestBody StudentProfileCreationRequest request) {
        userProfileService.createStudentProfile(request);
        return ApiResponse.empty();
    }

    @GetMapping("/users/student/profile/{profileId}")
    ApiResponse<StudentProfileResponse> getStudentProfile(@PathVariable String profileId) {
        return ApiResponse.success(userProfileService.getStudentProfile(profileId));
    }

    @PutMapping("/users/student/profile/{profileId}")
    @PreAuthorize("hasRole('STUDENT')or hasRole('ADMIN')")
    ApiResponse<Void> updateStudentProfile(
            @PathVariable String profileId, @RequestBody UpdateStudentProfileRequest request) {
        userProfileService.updateStudentProfile(profileId, request);
        return ApiResponse.empty();
    }
}
