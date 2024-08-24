package com.shadcn.profileservice.service;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.shadcn.profileservice.dto.request.ProfileCreationRequest;
import com.shadcn.profileservice.dto.response.UserProfileResponse;
import com.shadcn.profileservice.entity.UserProfile;
import com.shadcn.profileservice.mapper.UserProfileMapper;
import com.shadcn.profileservice.repository.UserProfileRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserProfileService {
    UserProfileRepository userProfileRepository;
    UserProfileMapper userProfileMapper;

    @CachePut(value = "profiles", key = "#result.id")
    @CacheEvict(value = "profiles", allEntries = true)
    public UserProfileResponse createProfile(ProfileCreationRequest request) {
        log.info("Creating profile for user {}", request.getUserId());
        UserProfile userProfile = userProfileMapper.toUserProfile(request);
        userProfile = userProfileRepository.save(userProfile);

        return userProfileMapper.toUserProfileReponse(userProfile);
    }

    public UserProfileResponse getProfile(String id) {
        UserProfile userProfile =
                userProfileRepository.findById(id).orElseThrow(() -> new RuntimeException("Profile not found"));

        return userProfileMapper.toUserProfileReponse(userProfile);
    }

    @Cacheable("profiles")
    public List<UserProfileResponse> getAllProfiles() {
        log.info("Fetching all profiles from database");
        var profiles = userProfileRepository.findAll();

        return profiles.stream().map(userProfileMapper::toUserProfileReponse).toList();
    }
}
