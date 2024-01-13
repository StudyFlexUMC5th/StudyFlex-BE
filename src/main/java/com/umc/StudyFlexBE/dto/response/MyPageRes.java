package com.umc.StudyFlexBE.dto.response;

import lombok.*;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MyPageRes {
    private String name;
    private String email;
    private boolean isSchoolCertification;
    private String schoolName;

}
