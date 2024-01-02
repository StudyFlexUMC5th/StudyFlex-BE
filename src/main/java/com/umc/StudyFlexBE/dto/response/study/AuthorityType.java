package com.umc.StudyFlexBE.dto.response.study;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@AllArgsConstructor
@Getter
public enum AuthorityType {
    LEADER("leader"), MEMBER("member"), NON_MEMBER("non_member");

    private String authority;
}
