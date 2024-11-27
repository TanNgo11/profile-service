package com.shadcn.profileservice.service.impl;

import java.time.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.transaction.*;

import org.springframework.cache.annotation.*;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shadcn.profileservice.dto.request.*;
import com.shadcn.profileservice.dto.response.*;
import com.shadcn.profileservice.entity.AdminProfile;
import com.shadcn.profileservice.entity.QAdminProfile;
import com.shadcn.profileservice.exception.*;
import com.shadcn.profileservice.mapper.*;
import com.shadcn.profileservice.repository.AdminProfileRepository;
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
public class AdminProfileService implements IAdminProfileService {

    UserProfileMapper userProfileMapper;
    AdminProfileRepository adminProfileRepository;
    AuthorizeUser authorizeUser;
    JPAQueryFactory queryFactory;

    @CacheEvict(value = "adminProfiles", allEntries = true)
    @Override
    @Transactional
    public void createAdminProfile(AdminProfileCreationRequest request) {
        if (adminProfileRepository.existsByPhoneNumber(request.getPhoneNumber()))
            throw new AppException(ErrorCode.PHONE_EXISTED);
        AdminProfile adminProfile = userProfileMapper.toAdminProfile(request);
        adminProfile.setAvatarPath("/file-svc/download/default-avatar");
        adminProfile.setAdminId(generateAdminId());
        adminProfileRepository.save(adminProfile);
    }

    @Override
    public AdminProfileResponse getAdminProfileByUsername(String username) {
        AdminProfile adminProfile = adminProfileRepository
                .findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.ADMIN_NOT_EXISTED));

        return userProfileMapper.toAdminProfileReponse(adminProfile);
    }

    @Override
    //    @Cacheable("adminProfiles")
    public PageResponse<AdminProfileResponse> getAllAdminProfiles(int current, int pageSize) {
        log.info("Fetching all profiles from database for admin");

        Pageable pageable = PageRequest.of(current - 1, pageSize);
        Page<AdminProfile> profiles = adminProfileRepository.findAll(pageable);

        return ConverToPaginationResponse.toPageResponse(profiles, userProfileMapper::toAdminProfileReponse, current);
    }

    @Override
    public List<AdminProfileResponse> getAllAdminProfilesByIds(long[] ids) {
        log.info("Fetching all profiles from database for admin by ids: {}", ids);
        Set<AdminProfile> adminProfiles = new HashSet<>();
        List<Long> missingIds = new ArrayList<>();

        for (long id : ids) {
            adminProfileRepository.findById(id).ifPresentOrElse(adminProfiles::add, () -> missingIds.add(id));
        }
        if (!missingIds.isEmpty()) {
            log.warn("Missing admin profiles with IDs: {}", missingIds);
        }
        return adminProfiles.stream()
                .map(userProfileMapper::toAdminProfileReponse)
                .toList();
    }

    @Override
    public List<AdminProfileResponse> getAllAdminProfilesByUsernames(String[] usernames) {
        log.info("Fetching all profiles from database for admin by usernames: {}", usernames);
        Set<AdminProfile> adminProfiles = new HashSet<>();
        List<String> missingUsernames = new ArrayList<>();

        for (String username : usernames) {
            adminProfileRepository
                    .findByUsername(username)
                    .ifPresentOrElse(adminProfiles::add, () -> missingUsernames.add(username));
        }

        if (!missingUsernames.isEmpty()) {
            log.warn("Missing admin profiles with usernames: {}", missingUsernames);
        }

        return adminProfiles.stream()
                .map(userProfileMapper::toAdminProfileReponse)
                .toList();
    }

    @Override
    @Transactional
    @CacheEvict(value = "adminProfiles", allEntries = true)
    @CachePut(value = "adminProfiles", key = "#id")
    public void updateAdminProfile(String id, UpdateAdminProfileRequest request) {

        AdminProfile existingProfile = adminProfileRepository
                .findByAdminId(id)
                .orElseThrow(() -> new AppException(ErrorCode.ADMIN_NOT_EXISTED));
        authorizeUser.checkAuthorizeUser();
        userProfileMapper.updateAdminProfileFromRequest(request, existingProfile);
        adminProfileRepository.save(existingProfile);
    }

    @Override
    public List<AdminProfileResponse> filterAdmins(AdminFilterRequest filterRequest) {
        QAdminProfile admin = QAdminProfile.adminProfile; // Generated by QueryDSL
        BooleanBuilder builder = new BooleanBuilder();

        if (filterRequest.getFirstName() != null) {
            builder.and(admin.firstName.containsIgnoreCase(filterRequest.getFirstName()));
        }
        if (filterRequest.getLastName() != null) {
            builder.and(admin.lastName.containsIgnoreCase(filterRequest.getLastName()));
        }
        if (filterRequest.getEmail() != null) {
            builder.and(admin.email.containsIgnoreCase(filterRequest.getEmail()));
        }
        if (filterRequest.getPhoneNumber() != null) {
            builder.and(admin.phoneNumber.eq(filterRequest.getPhoneNumber()));
        }
        if (filterRequest.getUsername() != null) {
            builder.and(admin.username.containsIgnoreCase(filterRequest.getUsername()));
        }
        if (filterRequest.getGender() != null) {
            builder.and(admin.gender.eq(filterRequest.getGender()));
        }
        if (filterRequest.getAddress() != null) {
            builder.and(admin.address.containsIgnoreCase(filterRequest.getAddress()));
        }
        if (filterRequest.getHireDate() != null) {
            builder.and(admin.hireDate.eq(filterRequest.getHireDate()));
        }
        if (filterRequest.getDepartmentId() != null) {
            builder.and(admin.departmentId.eq(filterRequest.getDepartmentId()));
        }
        if (filterRequest.getWorkSchedule() != null) {
            builder.and(admin.workSchedule.containsIgnoreCase(filterRequest.getWorkSchedule()));
        }
        if (filterRequest.getEmergencyContactName() != null) {
            builder.and(admin.emergencyContactName.containsIgnoreCase(filterRequest.getEmergencyContactName()));
        }
        if (filterRequest.getEmergencyContactPhoneNumber() != null) {
            builder.and(admin.emergencyContactPhoneNumber.eq(filterRequest.getEmergencyContactPhoneNumber()));
        }
        List<AdminProfile> admins = queryFactory.selectFrom(admin).where(builder).fetch();

        return admins.stream()
                .map(userProfileMapper::toAdminProfileReponse)
                .toList();
    }

    private String generateAdminId() {
        String year = String.valueOf(Year.now().getValue());

        AdminProfile lastProfile = adminProfileRepository.findTopByOrderByIdDesc();
        String lastAdminId = lastProfile != null ? lastProfile.getAdminId() : null;

        int orderNumber = 1;
        if (lastAdminId != null && lastAdminId.startsWith(year)) {
            orderNumber = Integer.parseInt(lastAdminId.substring(4)) + 1;
        }

        // Format the new admin_id as Year + 6 digit order number (e.g., 2024000001)
        return year + String.format("%06d", orderNumber);
    }
}
