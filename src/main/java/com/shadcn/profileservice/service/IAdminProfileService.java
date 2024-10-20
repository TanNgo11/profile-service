package com.shadcn.profileservice.service;

import com.shadcn.profileservice.dto.request.*;
import com.shadcn.profileservice.dto.response.*;

public interface IAdminProfileService {

    void createAdminProfile(AdminProfileCreationRequest request);

    AdminProfileResponse getAdminProfileByUsername(String username);

    PageResponse<AdminProfileResponse> getAllAdminProfiles(int current, int pageSize);

    void updateAdminProfile(String id, UpdateAdminProfileRequest request);
}
