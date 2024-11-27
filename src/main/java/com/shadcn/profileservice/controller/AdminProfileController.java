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
import com.shadcn.profileservice.entity.AdminProfile;
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
    ApiResponse<AdminProfileResponse> getAdminProfileByUsername(@PathVariable String username) {
        return ApiResponse.success(adminProfileService.getAdminProfileByUsername(username));
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

    @GetMapping("/users/admins/ids")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<List<AdminProfileResponse>> getAllAdminProfilesByIds(@RequestParam long[] adminIds) {
        return ApiResponse.success(adminProfileService.getAllAdminProfilesByIds(adminIds));
    }

    //    @GetMapping("/users/admins/usernames")
    //    @PreAuthorize("hasRole('ADMIN')")
    //    public ApiResponse<List<AdminProfileResponse>> getAllAdminProfilesByUsernames(@RequestParam String[]
    // usernames) {
    //        return ApiResponse.success(adminProfileService.getAllAdminProfilesByUsernames(usernames));
    //    }

    @GetMapping("/filter")
    public ApiResponse<List<AdminProfileResponse>> filterAdmins(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phoneNumber,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) Gender gender,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) String hireDate,
            @RequestParam(required = false) String departmentId,
            @RequestParam(required = false) String workSchedule,
            @RequestParam(required = false) String emergencyContactName,
            @RequestParam(required = false) String emergencyContactPhoneNumber
    ) {
        // Chuyển đổi các tham số thành AdminFilterRequest
        AdminFilterRequest filterRequest = AdminFilterRequest.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .phoneNumber(phoneNumber)
                .username(username)
                .gender(gender)
                .address(address)
                .hireDate(hireDate != null ? LocalDate.parse(hireDate, DateTimeFormatter.ofPattern("dd-MM-yyyy")) : null)
                .departmentId(departmentId)
                .workSchedule(workSchedule)
                .emergencyContactName(emergencyContactName)
                .emergencyContactPhoneNumber(emergencyContactPhoneNumber)
                .build();

        return ApiResponse.success(adminProfileService.filterAdmins(filterRequest));
    }
}
