package ru.sentidas.country.ex;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.sentidas.country.model.Country;
import ru.sentidas.country.model.Error;

import java.time.LocalDateTime;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    @Value("${spring.application.name}")
    private String appName;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Error> handleGenericException(Exception ex,
                                                        HttpServletRequest request) {

        LOG.error("Resolve exception in @RestControllerAdvice ", ex);
        return withStatus("Internal error",INTERNAL_SERVER_ERROR, ex.getMessage(), request);

    }

    @ExceptionHandler({
            VisitNotFoundException.class,
            UserNotFoundException.class
    })
    public ResponseEntity<Error> handleEntityNotFound(Exception ex,
                                                      HttpServletRequest request) {
        LOG.warn("Resolve exception in @RestControllerAdvice ", ex);
        return withStatus("Not Found", NOT_FOUND, ex.getMessage(), request);
    }



    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Error> handleInvalidFormatException(HttpMessageNotReadableException ex,
                                                              HttpServletRequest request) {

        LOG.warn("Resolve exception in @RestControllerAdvice ", ex);

        String errorMessage = ex.getMessage();

        Throwable cause = ex.getCause();
        if (cause instanceof InvalidFormatException) {
            InvalidFormatException ife = (InvalidFormatException) cause;
            if (ife.getTargetType() != null && ife.getTargetType().equals(Country.class)) {
                errorMessage = "Invalid country";
            }
        }

        return withStatus("Bad request", BAD_REQUEST, errorMessage, request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Error> handleValidationException(MethodArgumentNotValidException ex,
                                                           HttpServletRequest request) {

        LOG.warn("Resolve exception in @RestControllerAdvice ", ex);

        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(f -> f.getDefaultMessage())
                .findFirst()
                .orElse("Validation error");

        return withStatus("Bad request", BAD_REQUEST, errorMessage, request);

    }


    @ExceptionHandler(exception = {
            IllegalArgumentException.class,
            IllegalStateException.class,
            BadRequestException.class

    })
    public ResponseEntity<Error> handleBadRequest(Exception ex, HttpServletRequest request) {
        LOG.warn("Resolve exception in @RestControllerAdvice ", ex);
        return withStatus("Bad request", BAD_REQUEST, ex.getMessage(), request);
    }

    private ResponseEntity<Error> withStatus(String type,
                                             HttpStatus status,
                                             String message,
                                             HttpServletRequest request) {
        return ResponseEntity
                .status(status)
                .body(new Error(
                        appName + ": " + type,
                        status.getReasonPhrase(),
                        status.value(),
                        message,
                        request.getRequestURI()
                ));
    }
}
