package com.shadcn.profileservice.controller;

import static com.shadcn.profileservice.constant.PathConstant.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.shadcn.profileservice.enums.Gender;
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
            @RequestParam(required = false) String teacherId,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String hireDate, // Format: dd-MM-yyyy
            @RequestParam(required = false) String departmentId,
            @RequestParam(required = false) Double salary,
            @RequestParam(required = false) String officeHours,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) String emergencyContactName,
            @RequestParam(required = false) String emergencyContactPhoneNumber,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String dateOfBirth, // Format: dd-MM-yyyy
            @RequestParam(required = false) String phoneNumber,
            @RequestParam(required = false) Gender gender,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String avatarPath,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortDirection,
            @RequestParam(defaultValue = "1", required = false) Integer current,
            @RequestParam(defaultValue = "10", required = false) Integer pageSize) {

        TeacherFilterRequest filterRequest = TeacherFilterRequest.builder()
                .teacherId(teacherId)
                .username(username)
                .hireDate(hireDate != null ? LocalDate.parse(hireDate, DateTimeFormatter.ofPattern("dd-MM-yyyy")) : null)
                .departmentId(departmentId)
                .salary(salary)
                .officeHours(officeHours)
                .address(address)
                .emergencyContactName(emergencyContactName)
                .emergencyContactPhoneNumber(emergencyContactPhoneNumber)
                .firstName(firstName)
                .lastName(lastName)
                .dateOfBirth(dateOfBirth != null ? LocalDate.parse(dateOfBirth, DateTimeFormatter.ofPattern("dd-MM-yyyy")) : null)
                .phoneNumber(phoneNumber)
                .gender(gender)
                .email(email)
                .avatarPath(avatarPath)
                .sortBy(sortBy != null ? sortBy : "id") // Default sortBy to "id"
                .sortDirection(sortDirection != null ? sortDirection : "asc") // Default sortDirection to "asc"
                .build();

        return ApiResponse.success(teacherProfileService.getAllTeacherProfiles(filterRequest, current, pageSize));
    }

    @GetMapping("/users/teachers/ids")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<List<TeacherProfileResponse>> getAllTeacherProfilesByIds(@RequestParam long[] teacherIds) {
        return ApiResponse.success(teacherProfileService.getAllTeacherProfilesByIds(teacherIds));
    }

    //    @GetMapping("/users/teachers/usernames")
    //    @PreAuthorize("hasRole('ADMIN')")
    //    public ApiResponse<List<TeacherProfileResponse>> getAllTeacherProfilesByUsernames(@RequestParam String[]
    // usernames) {
    //        return ApiResponse.success(teacherProfileService.getAllTeacherProfilesByUsernames(usernames));
    //    }

}
