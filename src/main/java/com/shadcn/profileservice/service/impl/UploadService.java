package com.shadcn.profileservice.service.impl;

import com.shadcn.profileservice.dto.response.ImageResponse;
import com.shadcn.profileservice.repository.httpclient.UploadClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
public class UploadService {
    UploadClient uploadClient;
//hh
    public String uploadImageIfPresent(MultipartFile avatar) {
        if (avatar != null) {
            ImageResponse imageResponse = uploadClient.uploadFile(avatar).getResult();

            return  imageResponse.getDownloadUri();
        }
        return null;
    }
}