package com.ssafy.gumibom.domain.user.sms;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    // CustomExceptions.Exception 예외 처리
    @ExceptionHandler(CustomExceptions.Exception.class)
    public ResponseEntity<DefaultRes<String>> handleCustomException(CustomExceptions.Exception e) {
        HttpStatus status = HttpStatus.BAD_REQUEST; // 예외 유형에 따라 상태 코드를 조정할 수 있습니다.
        DefaultRes<String> response = DefaultRes.res(status.value(), e.getMessage());
        return new ResponseEntity<>(response, status);
    }

    // 기타 예외 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<DefaultRes<String>> handleGeneralException(Exception e) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR; // 일반 예외의 경우 일반적으로 500 상태 코드 사용
        DefaultRes<String> response = DefaultRes.res(status.value(), "An unexpected error occurred.");
        return new ResponseEntity<>(response, status);
    }

    // HttpMediaTypeNotAcceptableException 예외 처리 추가
    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    public ResponseEntity<DefaultRes<String>> handleHttpMediaTypeNotAcceptableException(HttpMediaTypeNotAcceptableException e) {
        HttpStatus status = HttpStatus.NOT_ACCEPTABLE; // 406 Not Acceptable
        DefaultRes<String> response = DefaultRes.res(status.value(), "Requested media type is not supported.");
        return new ResponseEntity<>(response, status);
    }
}