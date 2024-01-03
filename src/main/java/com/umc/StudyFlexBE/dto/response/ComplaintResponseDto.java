package com.umc.StudyFlexBE.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ComplaintResponseDto {
    private String complaintCategory;
    private String content;
}

