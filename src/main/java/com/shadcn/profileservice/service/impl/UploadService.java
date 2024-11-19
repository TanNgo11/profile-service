package com.shadcn.profileservice.service.impl;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.shadcn.profileservice.dto.response.ImageResponse;
import com.shadcn.profileservice.repository.httpclient.UploadClient;
import com.shadcn.profileservice.service.IUploadService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
public class UploadService implements IUploadService {
    UploadClient uploadClient;

    @Override
    public String uploadImageIfPresent(MultipartFile avatar) {
        if (avatar != null) {
            ImageResponse imageResponse = uploadClient.uploadFile(avatar).getResult();

            return imageResponse.getDownloadUri();
        }
        return null;
    }
}
