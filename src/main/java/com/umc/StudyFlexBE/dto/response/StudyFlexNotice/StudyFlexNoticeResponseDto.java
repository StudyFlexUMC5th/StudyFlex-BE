package com.umc.StudyFlexBE.dto.response.StudyFlexNotice;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@AllArgsConstructor
public class StudyFlexNoticeResponseDto {
    private Long id;
    private Integer view;
    private String title;
    private String content;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}