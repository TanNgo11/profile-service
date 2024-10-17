package com.shadcn.profileservice.service;

import com.shadcn.profileservice.dto.request.*;
import com.shadcn.profileservice.dto.response.*;

public interface IStudentProfileService {
    void createStudentProfile(StudentProfileCreationRequest request);

    void updateStudentProfile(String id, UpdateStudentProfileRequest request);

    StudentProfileResponse getStudentProfileByUsername(String username);

    PageResponse<StudentProfileResponse> getAllStudentProfiles(int current, int pageSize);
}
