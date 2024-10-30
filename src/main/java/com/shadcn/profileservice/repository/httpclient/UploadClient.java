package com.shadcn.profileservice.repository.httpclient;

import com.shadcn.profileservice.exception.RetreiveMessageErrorDecoder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.shadcn.profileservice.config.AuthenticationRequestInterceptor;
import com.shadcn.profileservice.dto.response.ApiResponse;
import com.shadcn.profileservice.dto.response.ImageResponse;

@FeignClient(
        name = "file-service",
        url = "${app.services.file}",
        configuration = {AuthenticationRequestInterceptor.class, RetreiveMessageErrorDecoder.class})

public interface UploadClient {
    @PostMapping(value = "/upload", headers = "Content-Type: multipart/form-data", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ApiResponse<ImageResponse> uploadFile(@RequestPart(value = "file") MultipartFile file);
}
