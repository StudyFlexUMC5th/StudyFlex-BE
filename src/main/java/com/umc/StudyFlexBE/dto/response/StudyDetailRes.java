package com.umc.StudyFlexBE.dto.response;

import com.umc.StudyFlexBE.entity.StudyStatus;
import lombok.*;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class StudyDetailRes {
    private int max_members;
    private int current_members;
    private StudyStatus study_status;
    private double total_progress_rate;
}
