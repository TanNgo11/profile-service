package com.shadcn.profileservice.service;

import com.shadcn.profileservice.dto.request.*;
import com.shadcn.profileservice.dto.response.*;

import java.util.List;

public interface IAdminProfileService {

    void createAdminProfile(AdminProfileCreationRequest request);

    AdminProfileResponse getAdminProfileByUsername(String username);

    PageResponse<AdminProfileResponse> getAllAdminProfiles(int current, int pageSize);

    List<AdminProfileResponse> getAllAdminProfilesByIds(long[] ids);

    List<AdminProfileResponse> getAllAdminProfilesByUsernames(String[] usernames);

    void updateAdminProfile(String id, UpdateAdminProfileRequest request);
}
