package com.shadcn.profileservice.mapper;

import com.shadcn.profileservice.dto.request.AdminProfileCreationRequest;
import com.shadcn.profileservice.dto.request.TeacherProfileCreationRequest;
import com.shadcn.profileservice.dto.response.AdminProfileResponse;
import com.shadcn.profileservice.dto.response.TeacherProfileResponse;
import com.shadcn.profileservice.entity.AdminProfile;
import com.shadcn.profileservice.entity.TeacherProfile;
import org.mapstruct.Mapper;

import com.shadcn.profileservice.dto.request.StudentProfileCreationRequest;
import com.shadcn.profileservice.dto.response.StudentProfileResponse;
import com.shadcn.profileservice.entity.StudentProfile;

@Mapper(componentModel = "spring")
public interface UserProfileMapper {
    StudentProfile toStudentProfile(StudentProfileCreationRequest request);

    TeacherProfile toTeacherProfile(TeacherProfileCreationRequest request);

    AdminProfile toAdminProfile(AdminProfileCreationRequest request);

    StudentProfileResponse toStudentProfileReponse(StudentProfile entity);

    TeacherProfileResponse toTeacherProfileReponse(TeacherProfile entity);

    AdminProfileResponse toAdminProfileReponse(AdminProfile entity);
}
