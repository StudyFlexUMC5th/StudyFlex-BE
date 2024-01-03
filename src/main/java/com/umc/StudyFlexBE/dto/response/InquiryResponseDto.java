package com.umc.StudyFlexBE.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@AllArgsConstructor
public class InquiryResponseDto {
    private Long id;
    private Integer view;
    private String title;
    private Boolean isOpen;
    private String writer;
    private String content;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Boolean isAnswered;
    private AnswerResponse answer;

    @Getter
    @AllArgsConstructor
    public static class AnswerResponse {
        private Long id;
        private String writer;
        private String content;
        private Timestamp createdAt;
        private Timestamp updatedAt;
    }
}

