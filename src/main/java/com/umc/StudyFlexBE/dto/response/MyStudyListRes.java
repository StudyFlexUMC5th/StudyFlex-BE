package com.umc.StudyFlexBE.dto.response;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MyStudyListRes {
    private int count;
    private List<MyStudyRes> myStudyList = new ArrayList<>();
}
