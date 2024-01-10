package com.umc.StudyFlexBE.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudyReq {
    @JsonProperty("thumbnail_url")
    private MultipartFile thumbnailUrl;
    @JsonProperty("study_name")
    private String studyName;
    @JsonProperty("max_members")
    private int maxMembers;
    @JsonProperty("category_name")
    private String categoryName;
    @JsonProperty("target_week")
    private int targetWeek;
}
