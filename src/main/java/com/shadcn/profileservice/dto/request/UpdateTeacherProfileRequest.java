package com.shadcn.profileservice.dto.request;

import lombok.*;
import lombok.experimental.*;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateTeacherProfileRequest extends BaseUpdateProfile {

    String department;
    String major;
    String emergencyContactName;
    String emergencyContactPhoneNumber;
}
