package com.miko.cruise19.controllers;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@RestControllerAdvice
@CrossOrigin(value = {"http://localhost:4200"})
public class ControllersHandler {

    @ExceptionHandler(value = {EmptyResultDataAccessException.class, NoSuchElementException.class})
    private ResponseEntity<?> handle404(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("'Missing' entity");
    }
    @ExceptionHandler(value = {DataAccessException.class})
    private ResponseEntity<?> handleNotModifiedAndNotImplemented(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Something went wrong");
    }
}
