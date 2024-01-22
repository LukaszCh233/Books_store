package com.example.Book_Store.Exceptions;

import com.example.Book_Store.controller.AdministratorController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;



@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception e) {
        logger.error("Global exception handler: {}", e.getMessage(), e);
        return new ResponseEntity<>("An error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
