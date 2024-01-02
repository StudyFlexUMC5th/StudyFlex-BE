package com.umc.StudyFlexBE.dto.request;

public class StudyFlexNoticeUploadDto {
    private Long member_id;
    private String title;
    private String content;

    public Long getMemberId() {
        return member_id;
    }

    public String getTitle() {
        return title;
    }
    public String getContent() {
        return content;
    }
}
