package ru.proskyryakov.cbrcursondateadapter.adapter.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CursControllerAdvice {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleException(NotFoundException e) {
        ExceptionResponse response = new ExceptionResponse(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

}
