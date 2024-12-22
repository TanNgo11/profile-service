package com.shadcn.profileservice.service.impl;

import java.time.Year;
import java.util.*;

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
import com.shadcn.profileservice.dto.request.StudentFilterRequest;
import com.shadcn.profileservice.dto.request.StudentProfileCreationRequest;
import com.shadcn.profileservice.dto.request.UpdateStudentProfileRequest;
import com.shadcn.profileservice.dto.response.PageResponse;
import com.shadcn.profileservice.dto.response.StudentProfileResponse;
import com.shadcn.profileservice.entity.QStudentProfile;
import com.shadcn.profileservice.entity.StudentProfile;
import com.shadcn.profileservice.enums.Present;
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
    UploadService uploadService;
    JPAQueryFactory queryFactory;

    @Override
    @CacheEvict(value = "profiles", allEntries = true)
    @Transactional
    public void createStudentProfile(StudentProfileCreationRequest request) {
        if (studentProfileRepository.existsByPhoneNumber(request.getPhoneNumber()))
            throw new AppException(ErrorCode.PHONE_EXISTED);

        StudentProfile studentProfile = userProfileMapper.toStudentProfile(request);
        studentProfile.setStudentId(generateStudentId());
        studentProfile.setAvatarPath("/file-svc/download/default-avatar");
        studentProfile.setPresent(Present.STUDYING);
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
    public PageResponse<StudentProfileResponse> getAllStudentProfiles(
            StudentFilterRequest filterRequest, int current, int pageSize) {
        QStudentProfile student = QStudentProfile.studentProfile;

        BooleanBuilder builder = buildFilterConditions(filterRequest, student);

        OrderSpecifier<?> orderSpecifier = getOrderSpecifier(filterRequest, student);

        Pageable pageable = PageRequest.of(current - 1, pageSize);

        JPQLQuery<StudentProfile> query =
                queryFactory.selectFrom(student).where(builder).orderBy(orderSpecifier);
        long total = query.fetchCount();
        List<StudentProfile> studentProfiles =
                query.offset(pageable.getOffset()).limit(pageable.getPageSize()).fetch();

        return ConverToPaginationResponse.toPageResponse(
                new PageImpl<>(studentProfiles, pageable, total), userProfileMapper::toStudentProfileReponse, current);
    }

    private BooleanBuilder buildFilterConditions(StudentFilterRequest filterRequest, QStudentProfile student) {
        BooleanBuilder builder = new BooleanBuilder();

        if (filterRequest.getStudentId() != null) {
            builder.and(student.studentId.eq(filterRequest.getStudentId()));
        }
        if (filterRequest.getUsername() != null) {
            builder.and(student.username.containsIgnoreCase(filterRequest.getUsername()));
        }
        if (filterRequest.getFirstName() != null) {
            builder.and(student.firstName.containsIgnoreCase(filterRequest.getFirstName()));
        }
        if (filterRequest.getLastName() != null) {
            builder.and(student.lastName.containsIgnoreCase(filterRequest.getLastName()));
        }
        if (filterRequest.getAddress() != null) {
            builder.and(student.address.containsIgnoreCase(filterRequest.getAddress()));
        }
        if (filterRequest.getDateOfBirth() != null) {
            builder.and(student.dateOfBirth.eq(filterRequest.getDateOfBirth()));
        }
        if (filterRequest.getPhoneNumber() != null) {
            builder.and(student.phoneNumber.eq(filterRequest.getPhoneNumber()));
        }
        if (filterRequest.getGender() != null) {
            builder.and(student.gender.eq(filterRequest.getGender()));
        }
        if (filterRequest.getGpa() > 0) {
            builder.and(student.gpa.eq(filterRequest.getGpa()));
        }
        if (filterRequest.getEnrollmentDate() != null) {
            builder.and(student.enrollmentDate.eq(filterRequest.getEnrollmentDate()));
        }
        if (filterRequest.getDepartmentId() != null) {
            builder.and(student.departmentId.eq(Long.valueOf(filterRequest.getDepartmentId())));
        }
        if (filterRequest.getGuardianName() != null) {
            builder.and(student.guardianName.containsIgnoreCase(filterRequest.getGuardianName()));
        }
        if (filterRequest.getGuardianPhoneNumber() != null) {
            builder.and(student.guardianPhoneNumber.eq(filterRequest.getGuardianPhoneNumber()));
        }
        if (filterRequest.getEmail() != null) {
            builder.and(student.email.containsIgnoreCase(filterRequest.getEmail()));
        }
        if (filterRequest.getNationality() != null) {
            builder.and(student.nationality.containsIgnoreCase(filterRequest.getNationality()));
        }
        if (filterRequest.getReligion() != null) {
            builder.and(student.religion.containsIgnoreCase(filterRequest.getReligion()));
        }
        if (filterRequest.getDegreeLevel() != null) {
            builder.and(student.degreeLevel.containsIgnoreCase(filterRequest.getDegreeLevel()));
        }
        if (filterRequest.getAcademicYearId() != null) {
            builder.and(student.academicYearId.eq(filterRequest.getAcademicYearId()));
        }
        if (filterRequest.getPresent() != null) {
            builder.and(student.present.eq(filterRequest.getPresent()));
        }
        if (filterRequest.getAvatarPath() != null) {
            builder.and(student.avatarPath.containsIgnoreCase(filterRequest.getAvatarPath()));
        }

        return builder;
    }

    private OrderSpecifier<?> getOrderSpecifier(StudentFilterRequest filterRequest, QStudentProfile student) {
        boolean isAscending =
                filterRequest.getSortDirection() == null || "asc".equalsIgnoreCase(filterRequest.getSortDirection());
        String sortBy = filterRequest.getSortBy() != null ? filterRequest.getSortBy() : "id";
        OrderSpecifier<?> orderSpecifier;

        switch (sortBy) {
            case "id":
                orderSpecifier = isAscending ? student.id.asc() : student.id.desc();
                break;
            case "studentId":
                orderSpecifier = isAscending ? student.studentId.asc() : student.studentId.desc();
                break;
            case "username":
                orderSpecifier = isAscending ? student.username.asc() : student.username.desc();
                break;
            case "firstName":
                orderSpecifier = isAscending ? student.firstName.asc() : student.firstName.desc();
                break;
            case "lastName":
                orderSpecifier = isAscending ? student.lastName.asc() : student.lastName.desc();
                break;
            case "address":
                orderSpecifier = isAscending ? student.address.asc() : student.address.desc();
                break;
            case "dateOfBirth":
                orderSpecifier = isAscending ? student.dateOfBirth.asc() : student.dateOfBirth.desc();
                break;
            case "phoneNumber":
                orderSpecifier = isAscending ? student.phoneNumber.asc() : student.phoneNumber.desc();
                break;
            case "gender":
                orderSpecifier = isAscending ? student.gender.asc() : student.gender.desc();
                break;
            case "gpa":
                orderSpecifier = isAscending ? student.gpa.asc() : student.gpa.desc();
                break;
            case "enrollmentDate":
                orderSpecifier = isAscending ? student.enrollmentDate.asc() : student.enrollmentDate.desc();
                break;
            case "departmentId":
                orderSpecifier = isAscending ? student.departmentId.asc() : student.departmentId.desc();
                break;
            case "guardianName":
                orderSpecifier = isAscending ? student.guardianName.asc() : student.guardianName.desc();
                break;
            case "guardianPhoneNumber":
                orderSpecifier = isAscending ? student.guardianPhoneNumber.asc() : student.guardianPhoneNumber.desc();
                break;
            case "email":
                orderSpecifier = isAscending ? student.email.asc() : student.email.desc();
                break;
            case "nationality":
                orderSpecifier = isAscending ? student.nationality.asc() : student.nationality.desc();
                break;
            case "religion":
                orderSpecifier = isAscending ? student.religion.asc() : student.religion.desc();
                break;
            case "degreeLevel":
                orderSpecifier = isAscending ? student.degreeLevel.asc() : student.degreeLevel.desc();
                break;
            case "academicYearId":
                orderSpecifier = isAscending ? student.academicYearId.asc() : student.academicYearId.desc();
                break;
            case "present":
                orderSpecifier = isAscending ? student.present.asc() : student.present.desc();
                break;
            case "avatarPath":
                orderSpecifier = isAscending ? student.avatarPath.asc() : student.avatarPath.desc();
                break;
            default:
                log.warn("Invalid sort field: {}, defaulting to studentId", sortBy);
                orderSpecifier = student.studentId.asc();
        }

        return orderSpecifier;
    }

    @Override
    public List<StudentProfileResponse> getAllStudentByAcademicYearId(Long academicYearId) {
        log.info("Fetching all profiles from database for student by academic year ID: {}", academicYearId);
        Set<StudentProfile> studentProfiles = new HashSet<>();
        studentProfileRepository.findAllByAcademicYearId(academicYearId).forEach(studentProfiles::add);
        return studentProfiles.stream()
                .map(userProfileMapper::toStudentProfileReponse)
                .toList();
    }

    @Override
    public void deleteStudents(List<String> studentUsernames) {
        log.info("Deleting student profiles by usernames: {}", studentUsernames);
        List<String> missingUsernames = new ArrayList<>();

        authorizeUser.checkAuthorizeUser();
        for (String username : studentUsernames) {
            studentProfileRepository
                    .findByUsername(username)
                    .ifPresentOrElse(studentProfileRepository::delete, () -> missingUsernames.add(username));
        }

        if (!missingUsernames.isEmpty()) {
            log.warn("Student profiles not found for IDs: {}", missingUsernames);
        }
    }

    @Override
    public List<StudentProfileResponse> getAllStudentProfilesByIds(long[] ids) {
        log.info("Fetching student profiles by IDs: {}", Arrays.toString(ids));
        Set<StudentProfile> studentProfiles = new HashSet<>();
        List<Long> missingIds = new ArrayList<>();

        for (long id : ids) {
            studentProfileRepository.findById(id).ifPresentOrElse(studentProfiles::add, () -> missingIds.add(id));
        }

        if (!missingIds.isEmpty()) {
            log.warn("Student profiles not found for IDs: {}", missingIds);
        }

        return studentProfiles.stream()
                .map(userProfileMapper::toStudentProfileReponse)
                .toList();
    }

    @Override
    public StudentProfileResponse getStudentProfileById(Long id) {
        StudentProfile studentProfile = studentProfileRepository
                .findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.STUDENT_NOT_EXISTED));

        return userProfileMapper.toStudentProfileReponse(studentProfile);
    }

    @Override
    public List<StudentProfileResponse> getAllStudentProfilesByUsernames(List<String> usernames) {

        Set<StudentProfile> studentProfiles = new HashSet<>();
        studentProfileRepository.findAllByUsernameIn(usernames).forEach(studentProfiles::add);
        return studentProfiles.stream()
                .map(userProfileMapper::toStudentProfileReponse)
                .toList();
    }

    @Override
    @Transactional
    @CacheEvict(value = "studentProfiles", allEntries = true)
    @CachePut(value = "studentProfiles", key = "#id")
    public void updateStudentProfile(Long id, UpdateStudentProfileRequest request) {
        StudentProfile existingProfile = studentProfileRepository
                .findById(id)
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
