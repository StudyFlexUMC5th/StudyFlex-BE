package com.umc.StudyFlexBE.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BaseException extends RuntimeException {

    private BaseResponseStatus status;
}
