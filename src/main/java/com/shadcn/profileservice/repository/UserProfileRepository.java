package com.shadcn.profileservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.shadcn.profileservice.entity.UserProfile;

@Repository
public interface UserProfileRepository extends MongoRepository<UserProfile, String> {}
