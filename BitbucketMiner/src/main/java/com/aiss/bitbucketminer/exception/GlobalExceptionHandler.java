package com.aiss.bitbucketminer.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Component
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BitbucketApiException.class)
    public ResponseEntity<String> handleBitbucketApi(BitbucketApiException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_GATEWAY)
                .body("Error al conectar con Bitbucket: " + ex.getMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> handleValidation(ConstraintViolationException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Parámetros inválidos: " + ex.getMessage());
    }
}




