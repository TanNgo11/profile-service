package com.shadcn.profileservice.repository;

import com.shadcn.profileservice.entity.AdminProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminProfileRepository extends JpaRepository<AdminProfile, String> {
}
