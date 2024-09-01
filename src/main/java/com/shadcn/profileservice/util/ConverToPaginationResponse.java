package com.shadcn.profileservice.util;

import com.shadcn.profileservice.dto.response.PageResponse;
import org.springframework.data.domain.Page;

import java.util.function.Function;

public class ConverToPaginationResponse {
    public static <T, R> PageResponse<R> toPageResponse(Page<T> pageData, Function<T, R> mapper, int currentPage) {
        return new PageResponse<>(
                currentPage,
                pageData.getSize(),
                pageData.getTotalPages(),
                pageData.getTotalElements(),
                pageData.getContent().stream().map(mapper).toList()
        );
    }
}
