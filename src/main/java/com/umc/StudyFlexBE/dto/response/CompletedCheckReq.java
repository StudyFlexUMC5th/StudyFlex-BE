package com.umc.StudyFlexBE.dto.response;

import lombok.*;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CompletedCheckReq {
    private int week;
    private double progress;
    private boolean completed;
}
