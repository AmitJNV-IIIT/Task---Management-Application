package com.example.taskmanager.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * GlobalExceptionHandler handles exceptions that occur throughout the application.
 * It provides centralized exception handling across all {@code @RequestMapping} methods in {@code @RestController} classes.
 *
 * <p>This class is annotated with {@code @RestControllerAdvice} to allow it to intercept exceptions and return custom responses.</p>
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles {@link TaskNotFoundException} thrown when a task is not found.
     *
     * @param ex the exception thrown when a task is not found.
     * @return a {@link ResponseEntity} containing the exception message and a {@link HttpStatus#NOT_FOUND} status code.
     */
    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<String> handleTaskNotFoundException(TaskNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    /**
     * Handles {@link UserNotFoundException} thrown when a user is not found.
     *
     * @param ex the exception thrown when a user is not found.
     * @return a {@link ResponseEntity} containing the exception message and a {@link HttpStatus#NOT_FOUND} status code.
     */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    /**
     * Handles {@link MethodArgumentNotValidException} thrown when method arguments fail validation.
     *
     * @param ex the exception thrown when method arguments are not valid.
     * @return a {@link ResponseEntity} containing the default validation error message and a {@link HttpStatus#BAD_REQUEST} status code.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationException(MethodArgumentNotValidException ex) {
        return new ResponseEntity<>(ex.getBindingResult().getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST);
    }
}
