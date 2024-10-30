package com.shadcn.profileservice.service;

import com.shadcn.profileservice.dto.request.*;
import com.shadcn.profileservice.dto.response.*;

import java.util.ArrayList;
import java.util.List;

public interface IStudentProfileService {
    void createStudentProfile(StudentProfileCreationRequest request);

    void updateStudentProfile(String id, UpdateStudentProfileRequest request);

    StudentProfileResponse getStudentProfileByUsername(String username);

    List<StudentProfileResponse> getAllStudentProfilesByIds(long[] ids);

    List<StudentProfileResponse> getAllStudentProfilesByUsernames(String[] usernames);

    PageResponse<StudentProfileResponse> getAllStudentProfiles(int current, int pageSize);
}
