package com.shadcn.profileservice.service;

import com.shadcn.profileservice.dto.request.AdminProfileCreationRequest;
import com.shadcn.profileservice.dto.request.StudentProfileCreationRequest;
import com.shadcn.profileservice.dto.request.TeacherProfileCreationRequest;
import com.shadcn.profileservice.dto.response.AdminProfileResponse;
import com.shadcn.profileservice.dto.response.PageResponse;
import com.shadcn.profileservice.dto.response.StudentProfileResponse;
import com.shadcn.profileservice.dto.response.TeacherProfileResponse;

public interface IUserProfileService {
    StudentProfileResponse createStudentProfile(StudentProfileCreationRequest request);

    TeacherProfileResponse createTeacherProfile(TeacherProfileCreationRequest request);

    AdminProfileResponse createAdminProfile(AdminProfileCreationRequest request);

    StudentProfileResponse getStudentProfile(String id);

    TeacherProfileResponse getTeacherProfile(String id);

    AdminProfileResponse getAdminProfile(String id);

    PageResponse<StudentProfileResponse> getAllStudentProfiles(int current, int pageSize);

    PageResponse<TeacherProfileResponse> getAllTeacherProfiles(int current, int pageSize);

    PageResponse<AdminProfileResponse> getAllAdminProfiles(int current, int pageSize);
}
