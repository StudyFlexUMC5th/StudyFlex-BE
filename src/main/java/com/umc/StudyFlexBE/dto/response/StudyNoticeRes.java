package com.umc.StudyFlexBE.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudyNoticeRes {
    private String title;
    private String content;
    private LocalDateTime createAt;
}
