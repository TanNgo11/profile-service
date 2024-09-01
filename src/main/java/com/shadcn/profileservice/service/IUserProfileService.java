package com.shadcn.profileservice.service;

import com.shadcn.profileservice.dto.request.ProfileCreationRequest;
import com.shadcn.profileservice.dto.response.PageResponse;
import com.shadcn.profileservice.dto.response.UserProfileResponse;

import java.util.List;

public interface IUserProfileService {
    UserProfileResponse createProfile(ProfileCreationRequest request);

    UserProfileResponse getProfile(String id);

    PageResponse<UserProfileResponse> getAllProfiles(int current, int pageSize);
}
