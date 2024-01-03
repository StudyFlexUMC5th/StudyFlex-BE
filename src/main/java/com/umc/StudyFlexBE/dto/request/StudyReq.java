package com.umc.StudyFlexBE.dto.request;

import jakarta.persistence.Column;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudyReq {
    @Column(name = "thumbnail_url")
    private MultipartFile thumbnailUrl;
    @Column(name = "study_name")
    private String studyName;
    @Column(name = "max_members")
    private int maxMembers;
    @Column(name = "category_name")
    private String categoryName;
    @Column(name = "target_week")
    private int targetWeek;
}
