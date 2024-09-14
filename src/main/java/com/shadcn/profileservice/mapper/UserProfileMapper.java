package com.shadcn.profileservice.mapper;

import org.mapstruct.*;

import com.shadcn.profileservice.dto.request.*;
import com.shadcn.profileservice.dto.response.*;
import com.shadcn.profileservice.entity.*;

@Mapper(componentModel = "spring")
public interface UserProfileMapper {

    // Student related mappings
    StudentProfile toStudentProfile(StudentProfileCreationRequest request);

    StudentProfileResponse toStudentProfileReponse(StudentProfile entity);

    void updateStudentProfileFromRequest(
            UpdateStudentProfileRequest request, @MappingTarget StudentProfile studentProfile);

    StudentProfile toStudentProfile(UpdateStudentProfileRequest request);

    // Teacher related mappings
    TeacherProfile toTeacherProfile(TeacherProfileCreationRequest request);

    TeacherProfileResponse toTeacherProfileReponse(TeacherProfile entity);

    void updateTeacherProfileFromRequest(
            UpdateTeacherProfileRequest request, @MappingTarget TeacherProfile teacherProfile);

    TeacherProfile toTeacherProfile(UpdateTeacherProfileRequest request);

    // Admin related mappings
    AdminProfile toAdminProfile(AdminProfileCreationRequest request);

    AdminProfileResponse toAdminProfileReponse(AdminProfile entity);

    void updateAdminProfileFromRequest(UpdateAdminProfileRequest request, @MappingTarget AdminProfile adminProfile);

    AdminProfile toAdminProfile(UpdateAdminProfileRequest request);
}
