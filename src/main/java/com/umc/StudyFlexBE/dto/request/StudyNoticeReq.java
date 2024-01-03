package com.umc.StudyFlexBE.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StudyNoticeReq {

    @NotBlank(message = "공지사항 제목은 공백일 수 없습니다.")
    @Size(max = 100, message = "공지사항 제목은 최대 100자 입니다.")
    private String title;

    @NotBlank(message = "공지사항 내용은 공백일 수 없습니다.")
    @Size(max = 1000, message = "공지사항 내용은 최대 1000자 입니다.")
    private String content;
}
