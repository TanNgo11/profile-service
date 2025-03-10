package com.shadcn.profileservice.service;

import java.util.List;

import com.shadcn.profileservice.dto.request.*;
import com.shadcn.profileservice.dto.response.*;

public interface ITeacherProfileService {

    void createTeacherProfile(TeacherProfileCreationRequest request);

    void updateTeacherProfile(String id, UpdateTeacherProfileRequest request);

    TeacherProfileResponse getTeacherProfileByUsername(String username);

    TeacherProfileResponse getPublicTeacherProfile(String id);

    List<TeacherProfileResponse> getAllTeacherProfilesByIds(long[] ids);

    List<TeacherProfileResponse> getAllTeacherProfilesByEntityIds(long[] teacherIds);

    List<TeacherProfileResponse> getAllTeacherProfilesByUsernames(String[] usernames);

    PageResponse<TeacherProfileResponse> getAllTeacherProfiles(
            TeacherFilterRequest filterRequest, int current, int pageSize);

    void deleteTeacherProfiles(String[] ids);

    void deleteTeacherProfileById(String id);

    TeacherProfileResponse getTeacherProfileByTeacherEntityId(long teacherId);
}
