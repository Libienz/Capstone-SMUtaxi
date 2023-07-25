package com.capstone.smutaxi.controller;

import com.capstone.smutaxi.dto.responses.ErrorResponse;
import com.capstone.smutaxi.dto.responses.ResponseFactory;
import com.capstone.smutaxi.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

@ControllerAdvice
public class ExceptionController {


    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleIdDuplicateException(BusinessException ex) {
        ErrorResponse errorResponse = ResponseFactory.createErrorResponse(ex.getErrorCode().getCode(), ex.getErrorCode().getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<ErrorResponse> handleIOException(IOException ex) {
        ErrorResponse errorResponse = ResponseFactory.createErrorResponse("IOException", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler({MalformedURLException.class, FileNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleCannotLoadImageException(IOException ex) {
        ErrorResponse errorResponse = ResponseFactory.createErrorResponse("CannotAccessFileException", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
