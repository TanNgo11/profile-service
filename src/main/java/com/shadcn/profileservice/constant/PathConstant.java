package com.shadcn.profileservice.constant;

public class PathConstant {
    public static final String USERS = "/users";
    public static final String API_V1 = "/api/v1";
    public static final String API_V1_USERS = API_V1 + USERS;
    public static final String PROFILE = API_V1 + "/profile";

    public static final String[] PUBLIC_ENDPOINTS = {
            API_V1_USERS + "/student",
            API_V1_USERS + "/teacher",
            API_V1_USERS + "/admin",
            API_V1_USERS + "/student/**",
            API_V1_USERS + "/teacher/**",
            API_V1_USERS + "/admin/**",
            API_V1_USERS,
            API_V1_USERS + "/**",
            "/swagger-ui/**",
            "/v3/api-docs/**",
    };
}
