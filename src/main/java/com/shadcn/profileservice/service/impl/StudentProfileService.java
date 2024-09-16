package com.shadcn.profileservice.service.impl;

import java.time.*;

import jakarta.transaction.*;

import org.springframework.cache.annotation.*;
import org.springframework.data.domain.*;
import org.springframework.security.core.*;
import org.springframework.security.core.context.*;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.*;

import com.shadcn.profileservice.dto.request.*;
import com.shadcn.profileservice.dto.response.*;
import com.shadcn.profileservice.entity.*;
import com.shadcn.profileservice.exception.*;
import com.shadcn.profileservice.mapper.*;
import com.shadcn.profileservice.repository.*;
import com.shadcn.profileservice.service.*;
import com.shadcn.profileservice.util.*;
import com.shadcn.profileservice.validator.*;

import lombok.*;
import lombok.experimental.*;
import lombok.extern.slf4j.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class StudentProfileService implements IStudentProfileService {

    UserProfileMapper userProfileMapper;
    private final StudentProfileRepository studentProfileRepository;
    private final AuthorizeUser authorizeUser;

    @Override
    @CacheEvict(value = "profiles", allEntries = true)
    public void createStudentProfile(StudentProfileCreationRequest request) {
        StudentProfile studentProfile = userProfileMapper.toStudentProfile(request);
        studentProfile.setStudentId(generateStudentId());
        studentProfile = studentProfileRepository.save(studentProfile);

        userProfileMapper.toStudentProfileReponse(studentProfile);
    }

    @Override
    public StudentProfileResponse getStudentProfile(String id) {
        StudentProfile studentProfile = studentProfileRepository
                .findByStudentId(id)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

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
        authorizeUser.checkAuthorizeUser(existingProfile.getStudentId());

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
