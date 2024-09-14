package com.shadcn.profileservice.service;

import com.shadcn.profileservice.dto.request.*;
import com.shadcn.profileservice.dto.response.*;

public interface IStudentProfileService {
    void createStudentProfile(StudentProfileCreationRequest request);

    void updatStudentProfile(String id, UpdateStudentProfileRequest request);

    StudentProfileResponse getStudentProfile(String id);

    PageResponse<StudentProfileResponse> getAllStudentProfiles(int current, int pageSize);
}
