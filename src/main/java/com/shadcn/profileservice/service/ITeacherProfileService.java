package com.shadcn.profileservice.service;

import com.shadcn.profileservice.dto.request.*;
import com.shadcn.profileservice.dto.response.*;

public interface ITeacherProfileService {

    void createTeacherProfile(TeacherProfileCreationRequest request);

    void updateTeacherProfile(String id, UpdateTeacherProfileRequest request);

    TeacherProfileResponse getTeacherProfileByUsername(String username);

    TeacherProfileResponse getPublicTeacherProfile(String id);

    PageResponse<TeacherProfileResponse> getAllTeacherProfiles(int current, int pageSize);
}
