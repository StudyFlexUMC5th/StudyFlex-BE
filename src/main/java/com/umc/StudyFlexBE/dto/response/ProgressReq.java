package com.umc.StudyFlexBE.dto.response;

import lombok.*;

import java.time.LocalDate;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ProgressReq {
    private int week;
    private LocalDate start_date;
    private double participant_rate;
    private boolean completed;
}
