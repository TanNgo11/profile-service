package com.shadcn.profileservice.dto.request;

import lombok.*;
import lombok.experimental.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper=false)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateAdminProfileRequest extends BaseUpdateProfile {

    String emergencyContactName;
    String emergencyContactPhoneNumber;
}
