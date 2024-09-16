package com.shadcn.profileservice.service;

import org.springframework.security.core.*;

public interface IAuthenticationFacade {
    Authentication getAuthentication();
}
