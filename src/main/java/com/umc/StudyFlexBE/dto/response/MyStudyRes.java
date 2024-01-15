package com.umc.StudyFlexBE.dto.response;

import com.umc.StudyFlexBE.entity.StudyStatus;
import lombok.*;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MyStudyRes {
    private Long studyId;
    private String name;
    private StudyStatus status;
    private String thumbnailUrl;
}
