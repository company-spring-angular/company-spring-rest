package com.company.rest.controller;

import com.company.rest.exceptions.CompanyNotFoundException;
import com.company.rest.exceptions.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlingController {

    private static final Logger LOG = LoggerFactory.getLogger(ExceptionHandlingController.class.getName());

    public static final String INVALID_REQUEST_MESSAGE = "Invalid request";
    public static final String METHOD_NOT_ALLOWED_MESSAGE = "Method not allowed";
    public static final String COMPANY_NOT_FOUND_MESSAGE = "Company not found";

    @ExceptionHandler({ValidationException.class})
    public ResponseEntity<ErrorResponse> invalidData(ValidationException exception) {
        String message = exception.getMessage();
        LOG.warn("Validation error: {}", message);
        ErrorResponse errorResponse = new ErrorResponse(message);
        return new ResponseEntity<ErrorResponse>(errorResponse, createDefaultHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({CompanyNotFoundException.class})
    public ResponseEntity<ErrorResponse> companyNotFound(CompanyNotFoundException exception) {
        LOG.warn("Company not found: {}", exception.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(COMPANY_NOT_FOUND_MESSAGE);
        return new ResponseEntity<ErrorResponse>(errorResponse, createDefaultHeaders(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({HttpMessageNotReadableException.class})
    public ResponseEntity<ErrorResponse> invalidData(HttpMessageNotReadableException exception) {
        LOG.warn("Invalid data: {}", exception.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(INVALID_REQUEST_MESSAGE);
        return new ResponseEntity<ErrorResponse>(errorResponse, createDefaultHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    public ResponseEntity<ErrorResponse> invalidMethod(HttpRequestMethodNotSupportedException exception) {
        LOG.warn("Invalid method: ", exception.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(METHOD_NOT_ALLOWED_MESSAGE);
        return new ResponseEntity<ErrorResponse>(errorResponse, createDefaultHeaders(), HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<ErrorResponse> somethingWentWrong(Exception exception) {
        LOG.warn("Unknown exception", exception);
        ErrorResponse errorResponse = new ErrorResponse(exception.getMessage());
        return new ResponseEntity<ErrorResponse>(errorResponse, createDefaultHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private HttpHeaders createDefaultHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        return headers;
    }

    private class ErrorResponse {
        private String cause;

        public ErrorResponse(String cause) {
            super();
            this.cause = cause;
        }

        public String getCause() {
            return cause;
        }

    }
}
