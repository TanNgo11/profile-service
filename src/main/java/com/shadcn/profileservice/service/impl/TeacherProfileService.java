package com.shadcn.profileservice.service.impl;

import java.time.Year;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.transaction.Transactional;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.shadcn.profileservice.dto.request.TeacherProfileCreationRequest;
import com.shadcn.profileservice.dto.request.UpdateTeacherProfileRequest;
import com.shadcn.profileservice.dto.response.PageResponse;
import com.shadcn.profileservice.dto.response.TeacherProfileResponse;
import com.shadcn.profileservice.entity.TeacherProfile;
import com.shadcn.profileservice.exception.AppException;
import com.shadcn.profileservice.exception.ErrorCode;
import com.shadcn.profileservice.mapper.UserProfileMapper;
import com.shadcn.profileservice.repository.TeacherProfileRepository;
import com.shadcn.profileservice.service.ITeacherProfileService;
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
public class TeacherProfileService implements ITeacherProfileService {

    UserProfileMapper userProfileMapper;
    TeacherProfileRepository teacherProfileRepository;
    AuthorizeUser authorizeUser;
    UploadService uploadService;

    //    @CachePut(value = "teacherProfiles", key = "#result.id")
    @CacheEvict(value = "teacherProfiles", allEntries = true)
    @Override
    @Transactional
    public void createTeacherProfile(TeacherProfileCreationRequest request) {
        if (teacherProfileRepository.existsByPhoneNumber(request.getPhoneNumber()))
            throw new AppException(ErrorCode.PHONE_EXISTED);

        TeacherProfile teacherProfile = userProfileMapper.toTeacherProfile(request);
        teacherProfile.setAvatarPath("/file-svc/download/default-avatar");
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

        authorizeUser.checkAuthorizeUser();
        userProfileMapper.updateTeacherProfileFromRequest(request, existingProfile);
        teacherProfileRepository.save(existingProfile);
    }

    @Override
    public TeacherProfileResponse getTeacherProfileByUsername(String username) {

        TeacherProfile teacherProfile = teacherProfileRepository
                .findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.TEACHER_NOT_EXISTED));
        authorizeUser.checkAuthorizeUser();
        return userProfileMapper.toTeacherProfileReponse(teacherProfile);
    }

    @Override
    public TeacherProfileResponse getPublicTeacherProfile(String id) {
        TeacherProfile teacherProfile = teacherProfileRepository
                .findByTeacherId(id)
                .orElseThrow(() -> new AppException(ErrorCode.TEACHER_NOT_EXISTED));
        authorizeUser.checkAuthorizeUser();
        return userProfileMapper.toTeacherProfileReponse(teacherProfile);
    }

    @Override
    public PageResponse<TeacherProfileResponse> getAllTeacherProfiles(int current, int pageSize) {

        Pageable pageable = PageRequest.of(current - 1, pageSize);
        Page<TeacherProfile> profiles = teacherProfileRepository.findAll(pageable);

        return ConverToPaginationResponse.toPageResponse(profiles, userProfileMapper::toTeacherProfileReponse, current);
    }

    @Override
    public List<TeacherProfileResponse> getAllTeacherProfilesByIds(long[] ids) {
        log.info("Fetching teacher profiles by IDs: {}", ids);
        Set<TeacherProfile> teacherProfiles = new HashSet<>();
        List<Long> missingIds = new ArrayList<>();

        for (long id : ids) {
            teacherProfileRepository.findById(id).ifPresentOrElse(teacherProfiles::add, () -> missingIds.add(id));
        }
        if (!missingIds.isEmpty()) {
            log.warn("Teacher profiles not found for IDs: {}", missingIds);
        }
        return teacherProfiles.stream()
                .map(userProfileMapper::toTeacherProfileReponse)
                .toList();
    }

    @Override
    public List<TeacherProfileResponse> getAllTeacherProfilesByUsernames(String[] usernames) {
        log.info("Fetching teacher profiles by usernames: {}", usernames);
        Set<TeacherProfile> teacherProfiles = new HashSet<>();
        List<String> missingUsernames = new ArrayList<>();

        for (String username : usernames) {
            teacherProfileRepository
                    .findByUsername(username)
                    .ifPresentOrElse(teacherProfiles::add, () -> missingUsernames.add(username));
        }
        if (!missingUsernames.isEmpty()) {
            log.warn("Teacher profiles not found for usernames: {}", missingUsernames);
        }
        return teacherProfiles.stream()
                .map(userProfileMapper::toTeacherProfileReponse)
                .toList();
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
