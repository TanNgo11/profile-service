package com.shadcn.profileservice.service;

import java.util.List;

import com.shadcn.profileservice.dto.request.*;
import com.shadcn.profileservice.dto.response.*;

public interface IStudentProfileService {
    void createStudentProfile(StudentProfileCreationRequest request);

    void updateStudentProfile(Long id, UpdateStudentProfileRequest request);

    StudentProfileResponse getStudentProfileByUsername(String username);

    List<StudentProfileResponse> getAllStudentProfilesByIds(long[] ids);

    StudentProfileResponse getStudentProfileById(Long id);

    List<StudentProfileResponse> getAllStudentProfilesByUsernames(String[] usernames);

    PageResponse<StudentProfileResponse> getAllStudentProfiles(int current, int pageSize);
}
