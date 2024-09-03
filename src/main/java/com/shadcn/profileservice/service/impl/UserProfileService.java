package com.shadcn.profileservice.service.impl;

import com.shadcn.profileservice.dto.request.AdminProfileCreationRequest;
import com.shadcn.profileservice.dto.request.TeacherProfileCreationRequest;
import com.shadcn.profileservice.dto.response.AdminProfileResponse;
import com.shadcn.profileservice.dto.response.PageResponse;
import com.shadcn.profileservice.dto.response.StudentProfileResponse;
import com.shadcn.profileservice.dto.response.TeacherProfileResponse;
import com.shadcn.profileservice.entity.AdminProfile;
import com.shadcn.profileservice.entity.TeacherProfile;
import com.shadcn.profileservice.repository.AdminProfileRepository;
import com.shadcn.profileservice.repository.TeacherProfileRepository;
import com.shadcn.profileservice.service.IUserProfileService;
import com.shadcn.profileservice.util.ConverToPaginationResponse;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.shadcn.profileservice.dto.request.StudentProfileCreationRequest;
import com.shadcn.profileservice.entity.StudentProfile;
import com.shadcn.profileservice.mapper.UserProfileMapper;
import com.shadcn.profileservice.repository.StudentProfileRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserProfileService implements IUserProfileService {
    StudentProfileRepository userProfileRepository;
    UserProfileMapper userProfileMapper;
    private final TeacherProfileRepository teacherProfileRepository;
    private final AdminProfileRepository adminProfileRepository;
    private final StudentProfileRepository studentProfileRepository;

    @CachePut(value = "profiles", key = "#result.id")
    @CacheEvict(value = "profiles", allEntries = true)
    public StudentProfileResponse createStudentProfile(StudentProfileCreationRequest request) {
        log.info("Creating profile for user {}", request.getUserId());
        StudentProfile studentProfile = userProfileMapper.toStudentProfile(request);
        studentProfile = userProfileRepository.save(studentProfile);

        return userProfileMapper.toStudentProfileReponse(studentProfile);
    }

    @CachePut(value = "profiles", key = "#result.id")
    @CacheEvict(value = "profiles", allEntries = true)
    @Override
    public TeacherProfileResponse createTeacherProfile(TeacherProfileCreationRequest request) {
        log.info("Creating profile for teacher {}", request.getUserId());
        TeacherProfile teacherProfile = userProfileMapper.toTeacherProfile(request);
        teacherProfile = teacherProfileRepository.save(teacherProfile);

        return userProfileMapper.toTeacherProfileReponse(teacherProfile);
    }

    @CachePut(value = "profiles", key = "#result.id")
    @CacheEvict(value = "profiles", allEntries = true)
    @Override
    public AdminProfileResponse createAdminProfile(AdminProfileCreationRequest request) {
        log.info("Creating profile for admin {}", request.getUserId());
        AdminProfile adminProfile = userProfileMapper.toAdminProfile(request);
        adminProfile = adminProfileRepository.save(adminProfile);

        return userProfileMapper.toAdminProfileReponse(adminProfile);
    }

    public StudentProfileResponse getStudentProfile(String id) {
        StudentProfile studentProfile =
                userProfileRepository.findById(id).orElseThrow(() -> new RuntimeException("Profile not found"));

        return userProfileMapper.toStudentProfileReponse(studentProfile);
    }

    public TeacherProfileResponse getTeacherProfile(String id) {
        TeacherProfile teacherProfile =
                teacherProfileRepository.findById(id).orElseThrow(() -> new RuntimeException("Profile not found"));

        return userProfileMapper.toTeacherProfileReponse(teacherProfile);
    }

    public AdminProfileResponse getAdminProfile(String id) {
        AdminProfile adminProfile =
                adminProfileRepository.findById(id).orElseThrow(() -> new RuntimeException("Profile not found"));

        return userProfileMapper.toAdminProfileReponse(adminProfile);
    }

    @Cacheable("profiles")
    public PageResponse<StudentProfileResponse> getAllStudentProfiles(int current, int pageSize) {
        log.info("Fetching all profiles from database for student");

        Pageable pageable = PageRequest.of(current - 1, pageSize);
        Page<StudentProfile> profiles = studentProfileRepository.findAll(pageable);

        return ConverToPaginationResponse.toPageResponse(profiles, userProfileMapper::toStudentProfileReponse, current);
    }

    @Cacheable("profiles")
    public PageResponse<TeacherProfileResponse> getAllTeacherProfiles(int current, int pageSize) {
        log.info("Fetching all profiles from database for teacher");

        Pageable pageable = PageRequest.of(current - 1, pageSize);
        Page<TeacherProfile> profiles = teacherProfileRepository.findAll(pageable);

        return ConverToPaginationResponse.toPageResponse(profiles, userProfileMapper::toTeacherProfileReponse, current);
    }

    @Cacheable("profiles")
    public PageResponse<AdminProfileResponse> getAllAdminProfiles(int current, int pageSize) {
        log.info("Fetching all profiles from database for admin");

        Pageable pageable = PageRequest.of(current - 1, pageSize);
        Page<AdminProfile> profiles = adminProfileRepository.findAll(pageable);

        return ConverToPaginationResponse.toPageResponse(profiles, userProfileMapper::toAdminProfileReponse, current);
    }
}
