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
@PreAuthorize("hasRole('ADMIN')")
public class AdminProfileController {

    IAdminProfileService userProfileService;

    @PostMapping("/users/admin")
    ApiResponse<Void> createAdminProfile(@RequestBody AdminProfileCreationRequest request) {
        userProfileService.createAdminProfile(request);
        return ApiResponse.empty();
    }

    @GetMapping("/users/admin/{profileId}")
    AdminProfileResponse getAdminProfile(@PathVariable String profileId) {
        return userProfileService.getAdminProfile(profileId);
    }

    @GetMapping("/users/admin")
    ApiResponse<PageResponse<AdminProfileResponse>> getAllAdminProfiles(
            @RequestParam(defaultValue = "1", required = false) Integer current,
            @RequestParam(defaultValue = "10", required = false) Integer pageSize) {
        return ApiResponse.success(userProfileService.getAllAdminProfiles(current, pageSize));
    }

    @PutMapping("/users/admin/{profileId}")
    ApiResponse<Void> updateAdminProfile(
            @PathVariable String profileId, @RequestBody UpdateAdminProfileRequest request) {
        userProfileService.updateAdminProfile(profileId, request);
        return ApiResponse.empty();
    }
}
