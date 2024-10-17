package com.shadcn.profileservice.validator;

import org.springframework.security.core.*;
import org.springframework.security.core.context.*;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.*;

import com.shadcn.profileservice.exception.*;
import com.shadcn.profileservice.service.*;

import lombok.*;
import lombok.experimental.*;
import lombok.extern.slf4j.*;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
public class AuthorizeUser {
    IAuthenticationFacade authenticationFacade;

    public void checkAuthorizeUser() {
        Authentication myAuthentication = authenticationFacade.getAuthentication();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = null;

        // Extract username from JWT token if present
        if (authentication != null && authentication.isAuthenticated()) {
            Jwt jwt = (Jwt) authentication.getPrincipal();
            username = jwt.getClaimAsString("sub");
        }

        // Authorization check
        if (username == null || !username.equals(myAuthentication.getName())) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
    }
}
