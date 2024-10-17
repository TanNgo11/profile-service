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
public class AdminProfileService implements IAdminProfileService {

    UserProfileMapper userProfileMapper;
    AdminProfileRepository adminProfileRepository;
    AuthorizeUser authorizeUser;


    @CacheEvict(value = "adminProfiles", allEntries = true)
    @Override
    @Transactional
    public void createAdminProfile(AdminProfileCreationRequest request) {
        if (adminProfileRepository.existsByPhoneNumber(request.getPhoneNumber()))
            throw new AppException(ErrorCode.PHONE_EXISTED);

        AdminProfile adminProfile = userProfileMapper.toAdminProfile(request);
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
    @Cacheable("adminProfiles")
    public PageResponse<AdminProfileResponse> getAllAdminProfiles(int current, int pageSize) {
        log.info("Fetching all profiles from database for admin");

        Pageable pageable = PageRequest.of(current - 1, pageSize);
        Page<AdminProfile> profiles = adminProfileRepository.findAll(pageable);

        return ConverToPaginationResponse.toPageResponse(profiles, userProfileMapper::toAdminProfileReponse, current);
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
