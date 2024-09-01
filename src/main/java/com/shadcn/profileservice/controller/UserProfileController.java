package com.shadcn.profileservice.controller;

import com.shadcn.profileservice.dto.request.ProfileCreationRequest;
import com.shadcn.profileservice.dto.response.ApiResponse;
import com.shadcn.profileservice.dto.response.PageResponse;
import com.shadcn.profileservice.dto.response.UserProfileResponse;
import com.shadcn.profileservice.service.impl.UserProfileService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserProfileController {
    UserProfileService userProfileService;

    @PostMapping("/users")
    UserProfileResponse createProfile(@RequestBody ProfileCreationRequest request) {
        return userProfileService.createProfile(request);
    }

    @GetMapping("/users/{profileId}")
    UserProfileResponse getProfile(@PathVariable String profileId) {
        return userProfileService.getProfile(profileId);
    }

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<PageResponse<UserProfileResponse>> getAllProfiles(
         @RequestParam(defaultValue = "1", required = false)   Integer current,
         @RequestParam(defaultValue = "10", required = false)   Integer pageSize) {
        return ApiResponse.success(userProfileService.getAllProfiles(current, pageSize));
    }
}
