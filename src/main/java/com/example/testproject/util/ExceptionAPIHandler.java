package com.example.testproject.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionAPIHandler {
    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(DataNotFoundException e) {
        ErrorResponse response = new ErrorResponse
                ("Data was not found!",
                        System.currentTimeMillis());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);//404-статус
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(GoodCardNotFoundException e) {
        ErrorResponse response = new ErrorResponse("Goodcard was not found!",
                System.currentTimeMillis());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(GoodNotFoundException e) {
        ErrorResponse response = new ErrorResponse
                ("Good was not found!",
                        System.currentTimeMillis());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(CounterpartNotFoundException e) {
        ErrorResponse response = new ErrorResponse
                ("Counterpart is not found!",
                        System.currentTimeMillis());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(RuntimeException e) {
        ErrorResponse response = new ErrorResponse
                (e.getMessage(),
                        System.currentTimeMillis());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}
