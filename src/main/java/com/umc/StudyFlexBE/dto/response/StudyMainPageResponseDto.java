package com.umc.StudyFlexBE.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/*
"studyId": 1,
            "studyName": "파이썬 마스터",
            "thumbnailUrl": "https://example.com/images/study-python-thumbnail.jpg",
            "studyStatus": "모집중",
            "maxMembers": 5,
            "currentMembers": 3,
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudyMainPageResponseDto {
    private int studyId;
    private String studyName;
    private String thumbnailUrl;
    private String studyStatus;
    private int maxMembers;
    private int currentMembers;

}
