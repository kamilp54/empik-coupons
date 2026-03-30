package com.interview.empik.coupons.exception;

import com.interview.empik.coupons.model.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class DefaultExceptionHandler {

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach((error) -> errors.put(error.getField(), error.getDefaultMessage()));
        return new ResponseEntity<>(new ExceptionResponse(4000, errors), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = CouponValidationException.class)
    public ResponseEntity<ExceptionResponse> handleException(CouponValidationException ex) {
        return new ResponseEntity<>(new ExceptionResponse(ex.getErrorCode(), ex.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
