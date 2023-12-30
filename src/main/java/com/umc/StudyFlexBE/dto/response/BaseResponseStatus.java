package com.umc.StudyFlexBE.dto.response;

import lombok.Getter;

@Getter
public enum BaseResponseStatus {

    /**
     * 1000 : Success
     */
    SUCCESS(true, 1000, "요청에 성공했습니다."),

    /**
     * 2XXX : Common
     */
    BAD_REQUEST(false, 2000, "잘못된 매개변수입니다."),

    /**
     * 3XXX : Member
     */
    DUPLICATE_EMAIL(false,3000,"중복된 이메일입니다."),

    NO_SUCH_EMAIL(false,3001,"해당 이메일이 존재하지 않습니다."),

    WRONG_PASSWORD(false, 3002, "비밀번호가 틀렸습니다."), ;



    /**
     * 4XXX : Post
     */



    /**
     * 5xxx: Mypage
     */


    private final boolean isSuccess;
    private final int code;
    private final String message;


    BaseResponseStatus(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }

}