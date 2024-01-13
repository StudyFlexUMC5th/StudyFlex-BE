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
    INTERNAL_SERVER_ERROR(false, 2001, "서버 내부 오류가 발생했습니다."),
    INVALID_NUMBER(false, 2002, "잘못된 숫자 형식입니다."),

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
    MAIL_SEND_FAILED(false, 3007,"이메일 전송 실패" ),
    CHANGE_EMAIL_FAILED(false,3008 ,"이메일 변경 실패" ),
    CHANGE_PASSWORD_FAILED(false,3009 ,"비밀번호 변경 실패" ),
    NAVER_LOGIN_FAILED(false, 3010, "네이버 로그인 실패"),

    /**
     * 4XXX : Post
     */
    MEMBER_NOT_FOUND(false, 4000, "회원을 찾을 수 없습니다."),
    DUPLICATE_STUDY_NAME(false, 4001,"중복된 스터디 이름입니다."),
    NO_SUCH_CATEGORY(false,4002, "해당 카테고리를 찾을 수 없습니다"),
    STUDY_NOT_FOUND(false, 4003, "스터디를 찾을 수 없습니다."),
    LEADER_NOT_FOUND(false, 4004, "리더를 찾을 수 없습니다."),
    NO_AUTHORITY(false,4005,"권한이 없습니다."),
    NO_SUCH_STUDY_NOTICE(false,4006,"해당 스터디 공지사항을 찾을 수 없습니다."),


    /**
     * 5xxx: Mypage
     */
    GET_MY_STUDY_FAILED( false,5001,"내 스터디를 얻어오는데 실패했습니다."),
    GET_MY_PAGE_FAILED( false,5002,"내 정보를 얻어오는데 실패했습니다."),

    /**
     * 6XXX : Search
     */
    NO_SUCH_STUDY(false, 6000, "해당 스터디를 찾을 수 없습니다."),

    /**
     * 7XXX : Study
     */
    FULL_STUDY_MEMBER(false, 7001, "스터디 맴버가 이미 가득 찼습니다."),
    NO_STUDY_PARTICIPANT(false,7002, "스터디 참여 맴버가 아닙니다."),
    NO_SUCH_WEEK(false,7003, "해당 주차 학습을 찾을 수 없습니다."),

    /**
     * S3 : S3
     */
    UPLOAD_FAILED(false, 8001, "이미지 업로드에 실패했습니다."),
    FILE_DELETE_FAILED(false, 8002, "이미지 삭제에 실패했습니다."),
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