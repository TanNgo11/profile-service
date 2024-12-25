package com.shadcn.profileservice.service.impl;

import java.time.*;
import java.util.*;

import jakarta.transaction.*;

import org.springframework.cache.annotation.*;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
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
    public PageResponse<AdminProfileResponse> getAllAdminProfiles(
            AdminFilterRequest filterRequest, int current, int pageSize) {
        QAdminProfile admin = QAdminProfile.adminProfile;

        BooleanBuilder builder = buildFilterConditions(filterRequest, admin);

        OrderSpecifier<?> orderSpecifier = getOrderSpecifier(filterRequest, admin);

        Pageable pageable = PageRequest.of(current - 1, pageSize);

        JPQLQuery<AdminProfile> query =
                queryFactory.selectFrom(admin).where(builder).orderBy(orderSpecifier);
        long total = query.fetchCount();
        List<AdminProfile> adminProfiles =
                query.offset(pageable.getOffset()).limit(pageable.getPageSize()).fetch();

        return ConverToPaginationResponse.toPageResponse(
                new PageImpl<>(adminProfiles, pageable, total), userProfileMapper::toAdminProfileReponse, current);
    }

    private BooleanBuilder buildFilterConditions(AdminFilterRequest filterRequest, QAdminProfile admin) {
        BooleanBuilder builder = new BooleanBuilder();

        if (filterRequest.getFullName() != null) {
            builder.and(
                    new BooleanBuilder()
                            .or(admin.firstName.containsIgnoreCase(filterRequest.getFullName()))
                            .or(admin.lastName.containsIgnoreCase(filterRequest.getFullName()))
                            .or(admin.middleName.containsIgnoreCase(filterRequest.getFullName()))
            );
        }
        if (filterRequest.getEmail() != null) {
            builder.and(admin.email.containsIgnoreCase(filterRequest.getEmail()));
        }
        if (filterRequest.getPhoneNumber() != null) {
            builder.and(admin.phoneNumber.containsIgnoreCase(filterRequest.getPhoneNumber()));
        }
        if (filterRequest.getUsername() != null) {
            builder.and(admin.username.containsIgnoreCase(filterRequest.getUsername()));
        }
        if (filterRequest.getGender() != null) {
            builder.and(admin.gender.stringValue().containsIgnoreCase(filterRequest.getGender().toString()));
        }
        if (filterRequest.getAddress() != null) {
            builder.and(admin.address.containsIgnoreCase(filterRequest.getAddress()));
        }
        if (filterRequest.getHireDate() != null) {
            builder.and(admin.hireDate.stringValue().containsIgnoreCase(filterRequest.getHireDate().toString()));
        }
        if (filterRequest.getDepartmentId() != null) {
            builder.and(admin.departmentId.containsIgnoreCase(filterRequest.getDepartmentId()));
        }
        if (filterRequest.getWorkSchedule() != null) {
            builder.and(admin.workSchedule.containsIgnoreCase(filterRequest.getWorkSchedule()));
        }
        if (filterRequest.getEmergencyContactName() != null) {
            builder.and(admin.emergencyContactName.containsIgnoreCase(filterRequest.getEmergencyContactName()));
        }
        if (filterRequest.getEmergencyContactPhoneNumber() != null) {
            builder.and(admin.emergencyContactPhoneNumber.containsIgnoreCase(filterRequest.getEmergencyContactPhoneNumber()));
        }

        return builder;
    }

    private OrderSpecifier<?> getOrderSpecifier(AdminFilterRequest filterRequest, QAdminProfile admin) {
        String sortBy = filterRequest.getSortBy() != null ? filterRequest.getSortBy() : "id";
        boolean isAscending =
                filterRequest.getSortDirection() == null || "asc".equalsIgnoreCase(filterRequest.getSortDirection());
        OrderSpecifier<?> orderSpecifier;

        switch (sortBy) {
            case "id":
                orderSpecifier = isAscending ? admin.id.asc() : admin.id.desc();
                break;
            case "firstName":
                orderSpecifier = isAscending ? admin.firstName.asc() : admin.firstName.desc();
                break;
            case "lastName":
                orderSpecifier = isAscending ? admin.lastName.asc() : admin.lastName.desc();
                break;
            case "email":
                orderSpecifier = isAscending ? admin.email.asc() : admin.email.desc();
                break;
            case "phoneNumber":
                orderSpecifier = isAscending ? admin.phoneNumber.asc() : admin.phoneNumber.desc();
                break;
            case "username":
                orderSpecifier = isAscending ? admin.username.asc() : admin.username.desc();
                break;
            case "gender":
                orderSpecifier = isAscending ? admin.gender.asc() : admin.gender.desc();
                break;
            case "address":
                orderSpecifier = isAscending ? admin.address.asc() : admin.address.desc();
                break;
            case "hireDate":
                orderSpecifier = isAscending ? admin.hireDate.asc() : admin.hireDate.desc();
                break;
            case "departmentId":
                orderSpecifier = isAscending ? admin.departmentId.asc() : admin.departmentId.desc();
                break;
            case "workSchedule":
                orderSpecifier = isAscending ? admin.workSchedule.asc() : admin.workSchedule.desc();
                break;
            case "emergencyContactName":
                orderSpecifier = isAscending ? admin.emergencyContactName.asc() : admin.emergencyContactName.desc();
                break;
            case "emergencyContactPhoneNumber":
                orderSpecifier = isAscending
                        ? admin.emergencyContactPhoneNumber.asc()
                        : admin.emergencyContactPhoneNumber.desc();
                break;
            default:
                orderSpecifier = admin.id.asc();
        }

        return orderSpecifier;
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
    public void updateAdminProfile(Long id, UpdateAdminProfileRequest request) {

        AdminProfile existingProfile =
                adminProfileRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.ADMIN_NOT_EXISTED));
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

    @Override
    public AdminProfileResponse getAdminProfileById(Long id) {
        AdminProfile adminProfile =
                adminProfileRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.ADMIN_NOT_EXISTED));

        return userProfileMapper.toAdminProfileReponse(adminProfile);
    }
}
