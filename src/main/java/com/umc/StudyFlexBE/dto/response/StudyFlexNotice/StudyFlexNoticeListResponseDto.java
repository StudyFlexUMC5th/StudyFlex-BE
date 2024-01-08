package com.umc.StudyFlexBE.dto.response.StudyFlexNotice;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;
import java.util.List;

@Getter
@AllArgsConstructor
public class StudyFlexNoticeListResponseDto {
    private int page;
    private int itemSize;
    private int totalPages;
    private List<NoticeSummary> noticeList;

    @Getter
    @AllArgsConstructor
    public static class NoticeSummary {
        private Long id;
        private String title;
        private int views;
    }
}
