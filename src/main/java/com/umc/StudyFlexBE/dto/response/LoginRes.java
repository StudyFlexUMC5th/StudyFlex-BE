package com.umc.StudyFlexBE.dto.response;

import lombok.*;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginRes {
    String token;
    String email;
}
