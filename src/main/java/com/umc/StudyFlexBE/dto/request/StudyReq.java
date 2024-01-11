package com.umc.StudyFlexBE.dto.request;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudyReq {

    private MultipartFile thumbnail_url;
    private String study_name;
    private String max_members;
    private String category_name;
    private String target_week;
}
