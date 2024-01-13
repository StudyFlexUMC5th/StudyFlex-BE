package com.umc.StudyFlexBE.dto.response;

import lombok.*;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class GetParticipationStudyRes {
    private Boolean isParticipation;
    private Long studyId;
}
