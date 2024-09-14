package com.shadcn.profileservice.dto.request;

import lombok.*;
import lombok.experimental.*;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateStudentProfileRequest extends BaseUpdateProfile {

    String guardianName;
    String guardianPhoneNumber;
}
