package com.umc.StudyFlexBE.dto.response;

import lombok.*;

import java.time.LocalDate;
import java.util.Optional;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ProgressRes {
    private int week;
    private LocalDate start_date;
    private double participant_rate;
    private Optional<Boolean> completed;
}
