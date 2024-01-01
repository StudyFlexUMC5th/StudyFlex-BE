package com.umc.StudyFlexBE.exception;

import com.umc.StudyFlexBE.dto.response.BaseResponse;
import com.umc.StudyFlexBE.dto.response.BaseResponseStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ValidExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public  ResponseEntity<BaseResponse<?>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        StringBuffer errorMessage = new StringBuffer();

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errorMessage.append(error.getDefaultMessage()).append(", ");
        });

        // Remove the trailing comma and space
        errorMessage.setLength(errorMessage.length() - 2);

        return new ResponseEntity<>(new BaseResponse<String>(BaseResponseStatus.BAD_REQUEST, errorMessage.toString()),
                HttpStatus.BAD_REQUEST);
    }
}