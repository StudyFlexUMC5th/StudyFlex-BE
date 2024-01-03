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
    DUPLICATE_EMAIL(false, 3000, "중복된 이메일입니다."),

    NO_SUCH_EMAIL(false, 3001, "해당 이메일이 존재하지 않습니다."),

    WRONG_PASSWORD(false, 3002, "비밀번호가 틀렸습니다."),


    GET_OAUTH_TOKEN_FAILED(false, 3003, "oAuth 토큰 요청 실패"),

    GET_OAUTH_INFO_FAILED(false, 3004, "oAuth Info 요청 실패"),

    SEND_EMAIL_FAILED(false,3005 ,"인증코드 요청 실패" ),
    WEB_MAIL_CODE_FAILED(false, 3006, "인증코드 불일치"),

    /**
     * 4XXX : Post
     */
    DUPLICATE_STUDY_NAME(false, 4001,"중복된 스터디 이름입니다."),
    NO_SUCH_CATEGORY(false,4002, "해당 카테고리를 찾을 수 없습니다"),


    /**
     * 5xxx: Mypage
     */

    /**
     * 6XXX : Search
     */
    NO_SUCH_STUDY(false, 6000, "해당 스터디를 찾을 수 없습니다."),

    INTERNAL_SERVER_ERROR(false, 500, "서버 내부 오류가 발생했습니다."),


    ;

    private final boolean isSuccess;
    private final int code;
    private final String message;


    BaseResponseStatus(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }



}