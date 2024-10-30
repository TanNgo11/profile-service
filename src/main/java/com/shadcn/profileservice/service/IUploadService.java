package com.shadcn.profileservice.service;

import org.springframework.web.multipart.MultipartFile;


public interface IUploadService {
    String uploadImageIfPresent(MultipartFile avatar);
}