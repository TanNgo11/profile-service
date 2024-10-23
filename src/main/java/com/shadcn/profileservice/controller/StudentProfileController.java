package com.shadcn.profileservice.controller;

import static com.shadcn.profileservice.constant.PathConstant.*;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.shadcn.profileservice.dto.request.*;
import com.shadcn.profileservice.dto.response.*;
import com.shadcn.profileservice.service.*;

import lombok.*;
import lombok.experimental.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(API_V1)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class StudentProfileController {
    IStudentProfileService userProfileService;

    @PostMapping(value = "/users/student")
    ApiResponse<Void> createStudentProfile(@ModelAttribute StudentProfileCreationRequest request) {
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
