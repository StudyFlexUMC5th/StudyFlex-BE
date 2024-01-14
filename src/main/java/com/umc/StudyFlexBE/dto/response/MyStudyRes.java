package com.umc.StudyFlexBE.dto.response;

import lombok.*;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MyStudyRes {
    private Long studyId;
    private String name;

    private String thumbnailUrl;
}
