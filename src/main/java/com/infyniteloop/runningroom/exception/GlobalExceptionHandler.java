package com.infyniteloop.runningroom.exception;

import com.infyniteloop.runningroom.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles NotFoundException and returns a structured error response.
     *
     * @param ex the NotFoundException instance
     * @return ResponseEntity with error details and HTTP status 404
     */
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException ex) {
        var error = new ErrorResponse(
                "Not Found",
                ex.getMessage(),
                Instant.now()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    /**
     * Handles DuplicateResourceException and returns a structured error response.
     *
     * @param ex the DuplicateResourceException instance
     * @return ResponseEntity with error details and HTTP status 400
     */
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateResourceException(DuplicateResourceException ex) {
        var error = new ErrorResponse(
                "Duplicate Resource Exception",
                ex.getMessage(),
                Instant.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    /**
     * Handles all other exceptions and returns a structured error response.
     *
     * @param ex the Exception instance
     * @return ResponseEntity with error details and HTTP status 500
     */
    @ExceptionHandler(Exception.class) // fallback for all other exceptions
    public ResponseEntity<ErrorResponse> handleGeneral(Exception ex) {
        var error = new ErrorResponse(
                "Internal Error",
                ex.getMessage(),
                Instant.now()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

}