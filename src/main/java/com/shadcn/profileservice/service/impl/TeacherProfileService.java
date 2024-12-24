package com.shadcn.profileservice.service.impl;

import java.time.Year;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.transaction.Transactional;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shadcn.profileservice.dto.request.TeacherFilterRequest;
import com.shadcn.profileservice.dto.request.TeacherProfileCreationRequest;
import com.shadcn.profileservice.dto.request.UpdateTeacherProfileRequest;
import com.shadcn.profileservice.dto.response.PageResponse;
import com.shadcn.profileservice.dto.response.TeacherProfileResponse;
import com.shadcn.profileservice.entity.QTeacherProfile;
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
    JPAQueryFactory queryFactory;

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
                .findById(Long.parseLong(id))
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
                .findById(Long.parseLong(id))
                .orElseThrow(() -> new AppException(ErrorCode.TEACHER_NOT_EXISTED));
        authorizeUser.checkAuthorizeUser();
        return userProfileMapper.toTeacherProfileReponse(teacherProfile);
    }

    @Override
    public PageResponse<TeacherProfileResponse> getAllTeacherProfiles(
            TeacherFilterRequest filterRequest, int current, int pageSize) {
        QTeacherProfile teacher = QTeacherProfile.teacherProfile;

        BooleanBuilder builder = buildFilterConditions(filterRequest, teacher);

        OrderSpecifier<?> orderSpecifier = getOrderSpecifier(filterRequest, teacher);

        Pageable pageable = PageRequest.of(current - 1, pageSize);

        JPQLQuery<TeacherProfile> query =
                queryFactory.selectFrom(teacher).where(builder).orderBy(orderSpecifier);
        long total = query.fetchCount();
        List<TeacherProfile> teacherProfiles =
                query.offset(pageable.getOffset()).limit(pageable.getPageSize()).fetch();

        return ConverToPaginationResponse.toPageResponse(
                new PageImpl<>(teacherProfiles, pageable, total), userProfileMapper::toTeacherProfileReponse, current);
    }

    @Override
    public void deleteTeacherProfiles(String[] ids) {
        List<Long> missingIds = new ArrayList<>();

        authorizeUser.checkAuthorizeUser();
        for (String id : ids) {
            teacherProfileRepository
                    .findById(Long.parseLong(id))
                    .ifPresentOrElse(teacherProfileRepository::delete, () -> missingIds.add(Long.parseLong(id)));
        }

        if (!missingIds.isEmpty()) {
            log.warn("Teacher profiles not found for IDs: {}", missingIds);
        }
    }

    @Override
    public void deleteTeacherProfileById(String id) {
        authorizeUser.checkAuthorizeUser();
        teacherProfileRepository.deleteById(Long.parseLong(id));
    }

    private BooleanBuilder buildFilterConditions(TeacherFilterRequest filterRequest, QTeacherProfile teacher) {
        BooleanBuilder builder = new BooleanBuilder();

        if (filterRequest.getTeacherId() != null) {
            builder.and(teacher.teacherId.containsIgnoreCase(filterRequest.getTeacherId()));
        }
        if (filterRequest.getUsername() != null) {
            builder.and(teacher.username.containsIgnoreCase(filterRequest.getUsername()));
        }
        if (filterRequest.getHireDate() != null) {
            builder.and(teacher.hireDate.stringValue().containsIgnoreCase(filterRequest.getHireDate().toString()));
        }
        if (filterRequest.getDepartmentId() != null) {
            builder.and(teacher.departmentId.containsIgnoreCase(filterRequest.getDepartmentId()));
        }
        if (filterRequest.getSalary() != null) {
            builder.and(teacher.salary.stringValue().containsIgnoreCase(String.valueOf(filterRequest.getSalary())));
        }
        if (filterRequest.getOfficeHours() != null) {
            builder.and(teacher.officeHours.containsIgnoreCase(filterRequest.getOfficeHours()));
        }
        if (filterRequest.getAddress() != null) {
            builder.and(teacher.address.containsIgnoreCase(filterRequest.getAddress()));
        }
        if (filterRequest.getEmergencyContactName() != null) {
            builder.and(teacher.emergencyContactName.containsIgnoreCase(filterRequest.getEmergencyContactName()));
        }
        if (filterRequest.getEmergencyContactPhoneNumber() != null) {
            builder.and(teacher.emergencyContactPhoneNumber.containsIgnoreCase(filterRequest.getEmergencyContactPhoneNumber()));
        }
        if (filterRequest.getFirstName() != null) {
            builder.and(teacher.firstName.containsIgnoreCase(filterRequest.getFirstName()));
        }
        if (filterRequest.getLastName() != null) {
            builder.and(teacher.lastName.containsIgnoreCase(filterRequest.getLastName()));
        }
        if (filterRequest.getDateOfBirth() != null) {
            builder.and(teacher.dateOfBirth.stringValue().containsIgnoreCase(filterRequest.getDateOfBirth().toString()));
        }
        if (filterRequest.getPhoneNumber() != null) {
            builder.and(teacher.phoneNumber.containsIgnoreCase(filterRequest.getPhoneNumber()));
        }
        if (filterRequest.getGender() != null) {
            builder.and(teacher.gender.stringValue().containsIgnoreCase(filterRequest.getGender().toString()));
        }
        if (filterRequest.getEmail() != null) {
            builder.and(teacher.email.containsIgnoreCase(filterRequest.getEmail()));
        }
        if (filterRequest.getAvatarPath() != null) {
            builder.and(teacher.avatarPath.containsIgnoreCase(filterRequest.getAvatarPath()));
        }

        return builder;
    }


    private OrderSpecifier<?> getOrderSpecifier(TeacherFilterRequest filterRequest, QTeacherProfile teacher) {
        boolean isAscending = "asc".equalsIgnoreCase(filterRequest.getSortDirection());
        String sortBy = filterRequest.getSortBy() != null ? filterRequest.getSortBy() : "teacherId";

        OrderSpecifier<?> orderSpecifier;

        switch (sortBy) {
            case "id":
                orderSpecifier = isAscending ? teacher.id.asc() : teacher.id.desc();
                break;
            case "teacherId":
                orderSpecifier = isAscending ? teacher.teacherId.asc() : teacher.teacherId.desc();
                break;
            case "username":
                orderSpecifier = isAscending ? teacher.username.asc() : teacher.username.desc();
                break;
            case "hireDate":
                orderSpecifier = isAscending ? teacher.hireDate.asc() : teacher.hireDate.desc();
                break;
            case "departmentId":
                orderSpecifier = isAscending ? teacher.departmentId.asc() : teacher.departmentId.desc();
                break;
            case "salary":
                orderSpecifier = isAscending ? teacher.salary.asc() : teacher.salary.desc();
                break;
            case "officeHours":
                orderSpecifier = isAscending ? teacher.officeHours.asc() : teacher.officeHours.desc();
                break;
            case "address":
                orderSpecifier = isAscending ? teacher.address.asc() : teacher.address.desc();
                break;
            case "emergencyContactName":
                orderSpecifier = isAscending ? teacher.emergencyContactName.asc() : teacher.emergencyContactName.desc();
                break;
            case "emergencyContactPhoneNumber":
                orderSpecifier = isAscending
                        ? teacher.emergencyContactPhoneNumber.asc()
                        : teacher.emergencyContactPhoneNumber.desc();
                break;
            case "firstName":
                orderSpecifier = isAscending ? teacher.firstName.asc() : teacher.firstName.desc();
                break;
            case "lastName":
                orderSpecifier = isAscending ? teacher.lastName.asc() : teacher.lastName.desc();
                break;
            case "dateOfBirth":
                orderSpecifier = isAscending ? teacher.dateOfBirth.asc() : teacher.dateOfBirth.desc();
                break;
            case "phoneNumber":
                orderSpecifier = isAscending ? teacher.phoneNumber.asc() : teacher.phoneNumber.desc();
                break;
            case "gender":
                orderSpecifier = isAscending ? teacher.gender.asc() : teacher.gender.desc();
                break;
            case "email":
                orderSpecifier = isAscending ? teacher.email.asc() : teacher.email.desc();
                break;
            case "avatarPath":
                orderSpecifier = isAscending ? teacher.avatarPath.asc() : teacher.avatarPath.desc();
                break;
            default:
                log.warn("Invalid sort field: {}, defaulting to teacherId", sortBy);
                orderSpecifier = teacher.teacherId.asc();
        }

        return orderSpecifier;
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
