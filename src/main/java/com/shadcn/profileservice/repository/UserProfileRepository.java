package com.shadcn.profileservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shadcn.profileservice.entity.UserProfile;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, String> {}
