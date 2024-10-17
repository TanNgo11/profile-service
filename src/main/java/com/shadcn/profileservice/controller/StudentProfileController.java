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

    @PostMapping("/users/student")
    ApiResponse<Void> createStudentProfile(@RequestBody StudentProfileCreationRequest request) {
        userProfileService.createStudentProfile(request);
        return ApiResponse.empty();
    }

    @GetMapping("/users/student/{username}")
    ApiResponse<StudentProfileResponse> getStudentProfileByUsername(@PathVariable String username) {
        return ApiResponse.success(userProfileService.getStudentProfileByUsername(username));
    }

    @PutMapping("/users/student/{studentId}")
    @PreAuthorize("hasRole('STUDENT') or hasRole('ADMIN')")
    ApiResponse<Void> updateStudentProfile(
            @PathVariable String studentId, @RequestBody UpdateStudentProfileRequest request) {
        userProfileService.updateStudentProfile(studentId, request);
        return ApiResponse.empty();
    }
}
