package com.shadcn.profileservice.service.impl;

import java.time.Year;

import jakarta.transaction.Transactional;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.shadcn.profileservice.dto.request.StudentProfileCreationRequest;
import com.shadcn.profileservice.dto.request.UpdateStudentProfileRequest;
import com.shadcn.profileservice.dto.response.PageResponse;
import com.shadcn.profileservice.dto.response.StudentProfileResponse;
import com.shadcn.profileservice.entity.StudentProfile;
import com.shadcn.profileservice.exception.AppException;
import com.shadcn.profileservice.exception.ErrorCode;
import com.shadcn.profileservice.mapper.UserProfileMapper;
import com.shadcn.profileservice.repository.StudentProfileRepository;
import com.shadcn.profileservice.service.IStudentProfileService;
import com.shadcn.profileservice.util.ConverToPaginationResponse;
import com.shadcn.profileservice.validator.AuthorizeUser;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class StudentProfileService implements IStudentProfileService {

    UserProfileMapper userProfileMapper;
    StudentProfileRepository studentProfileRepository;
    AuthorizeUser authorizeUser;

    @Override
    @CacheEvict(value = "profiles", allEntries = true)
    public void createStudentProfile(StudentProfileCreationRequest request) {
        if (studentProfileRepository.existsByPhoneNumber(request.getPhoneNumber()))
            throw new AppException(ErrorCode.PHONE_EXISTED);

        StudentProfile studentProfile = userProfileMapper.toStudentProfile(request);
        studentProfile.setStudentId(generateStudentId());
        studentProfileRepository.save(studentProfile);
    }

    @Override
    public StudentProfileResponse getStudentProfileByUsername(String username) {
        StudentProfile studentProfile = studentProfileRepository
                .findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.STUDENT_NOT_EXISTED));

        return userProfileMapper.toStudentProfileReponse(studentProfile);
    }

    @Override
    @Cacheable("studentProfiles")
    public PageResponse<StudentProfileResponse> getAllStudentProfiles(int current, int pageSize) {
        log.info("Fetching all profiles from database for student");

        Pageable pageable = PageRequest.of(current - 1, pageSize);
        Page<StudentProfile> profiles = studentProfileRepository.findAll(pageable);

        return ConverToPaginationResponse.toPageResponse(profiles, userProfileMapper::toStudentProfileReponse, current);
    }

    @Override
    @Transactional
    @CacheEvict(value = "studentProfiles", allEntries = true)
    @CachePut(value = "studentProfiles", key = "#id")
    public void updateStudentProfile(String id, UpdateStudentProfileRequest request) {
        StudentProfile existingProfile = studentProfileRepository
                .findByStudentId(id)
                .orElseThrow(() -> new AppException(ErrorCode.STUDENT_NOT_EXISTED));
        authorizeUser.checkAuthorizeUser();

        userProfileMapper.updateStudentProfileFromRequest(request, existingProfile);

        studentProfileRepository.save(existingProfile);
    }

    private String generateStudentId() {
        String year = String.valueOf(Year.now().getValue());

        StudentProfile lastProfile = studentProfileRepository.findTopByOrderByIdDesc();
        String lastAdminId = lastProfile != null ? lastProfile.getStudentId() : null;

        int orderNumber = 1;
        if (lastAdminId != null && lastAdminId.startsWith(year)) {
            orderNumber = Integer.parseInt(lastAdminId.substring(4)) + 1;
        }

        return year + String.format("%06d", orderNumber);
    }
}
