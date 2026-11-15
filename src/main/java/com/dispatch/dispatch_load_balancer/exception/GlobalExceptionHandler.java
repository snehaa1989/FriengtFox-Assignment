
package com.dispatch.dispatch_load_balancer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(
            MethodArgumentNotValidException exception) {

        String message = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error ->
                        error.getField() + ": " + error.getDefaultMessage()
                )
                .distinct()
                .collect(Collectors.joining("; "));

        return ResponseEntity.badRequest().body(
                Map.of(
                        "status", "error",
                        "message", message
                )
        );
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalState(
            IllegalStateException exception) {

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(
                Map.of(
                        "status", "error",
                        "message", exception.getMessage()
                )
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneral(
            Exception exception) {

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                Map.of(
                        "status", "error",
                        "message", "An unexpected server error occurred."
                )
        );
    }
    @ExceptionHandler(IllegalArgumentException.class)
public ResponseEntity<Map<String, Object>> handleIllegalArgument(
        IllegalArgumentException exception) {

    return ResponseEntity.badRequest().body(
            Map.of(
                    "status", "error",
                    "message", exception.getMessage()
            )
    );
}
}
