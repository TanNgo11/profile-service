package com.shadcn.profileservice.service.impl;

import java.time.*;

import jakarta.transaction.*;

import org.springframework.cache.annotation.*;
import org.springframework.data.domain.*;
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
public class TeacherProfileService implements ITeacherProfileService {

    UserProfileMapper userProfileMapper;
    private final TeacherProfileRepository teacherProfileRepository;
    private final AuthorizeUser authorizeUser;


    //    @CachePut(value = "teacherProfiles", key = "#result.id")
    @CacheEvict(value = "teacherProfiles", allEntries = true)
    @Override
    public void createTeacherProfile(TeacherProfileCreationRequest request) {

        TeacherProfile teacherProfile = userProfileMapper.toTeacherProfile(request);
        teacherProfile.setTeacherId(generateTeacherId());
        teacherProfile = teacherProfileRepository.save(teacherProfile);

        userProfileMapper.toTeacherProfileReponse(teacherProfile);
    }

    @Override
    @Transactional
    @CacheEvict(value = "teacherProfiles", allEntries = true)
    @CachePut(value = "teacherProfiles", key = "#id")
    public void updateTeacherProfile(String id, UpdateTeacherProfileRequest request) {
        TeacherProfile existingProfile = teacherProfileRepository
                .findByTeacherId(id)
                .orElseThrow(() -> new AppException(ErrorCode.TEACHER_NOT_EXISTED));

        authorizeUser.checkAuthorizeUser(existingProfile.getTeacherId());
        userProfileMapper.updateTeacherProfileFromRequest(request, existingProfile);
        teacherProfileRepository.save(existingProfile);
    }

    @Override
    public TeacherProfileResponse getTeacherProfileById(String id) {

        TeacherProfile teacherProfile = teacherProfileRepository
                .findByTeacherId(id)
                .orElseThrow(() -> new AppException(ErrorCode.TEACHER_NOT_EXISTED));
        authorizeUser.checkAuthorizeUser(teacherProfile.getTeacherId());
        return userProfileMapper.toTeacherProfileReponse(teacherProfile);
    }

    @Override
    public TeacherProfileResponse getPublicTeacherProfile(String id) {
        TeacherProfile teacherProfile = teacherProfileRepository
                .findByTeacherId(id)
                .orElseThrow(() -> new AppException(ErrorCode.TEACHER_NOT_EXISTED));
        authorizeUser.checkAuthorizeUser(teacherProfile.getTeacherId());
        return userProfileMapper.toTeacherProfileReponse(teacherProfile);
    }

    @Override
    @Cacheable("teacherProfiles")
    public PageResponse<TeacherProfileResponse> getAllTeacherProfiles(int current, int pageSize) {

        Pageable pageable = PageRequest.of(current - 1, pageSize);
        Page<TeacherProfile> profiles = teacherProfileRepository.findAll(pageable);

        return ConverToPaginationResponse.toPageResponse(profiles, userProfileMapper::toTeacherProfileReponse, current);
    }

    private String generateTeacherId() {
        String year = String.valueOf(Year.now().getValue());

        TeacherProfile lastProfile = teacherProfileRepository.findTopByOrderByIdDesc();
        String lastAdminId = lastProfile != null ? lastProfile.getTeacherId() : null;

        int orderNumber = 1;
        if (lastAdminId != null && lastAdminId.startsWith(year)) {
            orderNumber = Integer.parseInt(lastAdminId.substring(4)) + 1;
        }

        // Format the new admin_id as Year + 6 digit order number (e.g., 2024000001)
        return year + String.format("%06d", orderNumber);
    }
}
