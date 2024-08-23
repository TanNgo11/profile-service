package com.shadcn.profileservice.mapper;

import org.mapstruct.Mapper;

import com.shadcn.profileservice.dto.request.ProfileCreationRequest;
import com.shadcn.profileservice.dto.response.UserProfileResponse;
import com.shadcn.profileservice.entity.UserProfile;

@Mapper(componentModel = "spring")
public interface UserProfileMapper {
    UserProfile toUserProfile(ProfileCreationRequest request);

    UserProfileResponse toUserProfileReponse(UserProfile entity);
}
