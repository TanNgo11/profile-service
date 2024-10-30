package com.shadcn.profileservice.service;

import com.shadcn.profileservice.dto.request.*;
import com.shadcn.profileservice.dto.response.*;

import java.util.List;

public interface ITeacherProfileService {

    void createTeacherProfile(TeacherProfileCreationRequest request);

    void updateTeacherProfile(String id, UpdateTeacherProfileRequest request);

    TeacherProfileResponse getTeacherProfileByUsername(String username);

    TeacherProfileResponse getPublicTeacherProfile(String id);

    List<TeacherProfileResponse> getAllTeacherProfilesByIds(long[] ids);

    List<TeacherProfileResponse> getAllTeacherProfilesByUsernames(String[] usernames);

    PageResponse<TeacherProfileResponse> getAllTeacherProfiles(int current, int pageSize);
}
