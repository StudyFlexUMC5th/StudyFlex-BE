package com.umc.StudyFlexBE.dto.response.Inquiry;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class InquiryListResponseDto {
    private int page;
    private int itemSize;
    private int totalPages;
    private List<InquirySummary> inquiryList;

    @Getter
    @AllArgsConstructor
    public static class InquirySummary {
        private Long id;
        private String title;
        private String writer;
        private int views;
        private boolean isOpen;
    }
}
