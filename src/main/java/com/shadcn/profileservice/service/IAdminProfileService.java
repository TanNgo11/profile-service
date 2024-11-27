package com.shadcn.profileservice.service;

import java.util.List;

import com.shadcn.profileservice.dto.request.*;
import com.shadcn.profileservice.dto.response.*;
import com.shadcn.profileservice.entity.AdminProfile;

public interface IAdminProfileService {

    void createAdminProfile(AdminProfileCreationRequest request);

    AdminProfileResponse getAdminProfileByUsername(String username);

    PageResponse<AdminProfileResponse> getAllAdminProfiles(int current, int pageSize);

    List<AdminProfileResponse> getAllAdminProfilesByIds(long[] ids);

    List<AdminProfileResponse> getAllAdminProfilesByUsernames(String[] usernames);

    void updateAdminProfile(String id, UpdateAdminProfileRequest request);

    List<AdminProfileResponse> filterAdmins(AdminFilterRequest filterRequest);
}
