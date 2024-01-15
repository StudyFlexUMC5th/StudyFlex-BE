package com.umc.StudyFlexBE.dto.response;

import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RankResponseDto {
    private int rank;
    private String teamName;
    private double progressRate;
    private int members;
    private String category;
}
