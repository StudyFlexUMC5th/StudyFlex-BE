package com.umc.StudyFlexBE.dto.response;

public class APICallException extends Exception {

    public APICallException(String message, Throwable cause) {
        super(message, cause);
    }
}
