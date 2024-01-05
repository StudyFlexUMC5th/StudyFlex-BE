package com.umc.StudyFlexBE.dto.response;

public class InvalidAuthorizationCodeException extends RuntimeException {

    public InvalidAuthorizationCodeException(String message) {
        super(message);
    }

}
