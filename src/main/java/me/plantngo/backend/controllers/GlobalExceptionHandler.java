package me.plantngo.backend.controllers;

import me.plantngo.backend.exceptions.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.security.core.AuthenticationException;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
            HttpHeaders headers, HttpStatus status, WebRequest request) {
        String defaultErrorMsg = ex.getAllErrors().get(0).getDefaultMessage();

        ErrorModel error = new ErrorModel(HttpStatus.BAD_REQUEST, "Validation Error: " + defaultErrorMsg,
                ex.getBindingResult().toString());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FailedRegistrationException.class)
    private ResponseEntity<ErrorModel> handleEntityNotFound(FailedRegistrationException ex) {
        ErrorModel error = new ErrorModel(HttpStatus.BAD_REQUEST, "Registration failed.", ex.getMessage());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthenticationException.class)
    private ResponseEntity<ErrorModel> handleAuthentication(AuthenticationException ex) {
        ErrorModel error = new ErrorModel(HttpStatus.BAD_REQUEST, "Invalid username or password.", ex.getMessage());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFoundException.class)
    private ResponseEntity<ErrorModel> handleUserNotFound(UserNotFoundException ex) {
        ErrorModel error = new ErrorModel(HttpStatus.NOT_FOUND, "User Not Found.", ex.getMessage());

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidUserTypeException.class)
    private ResponseEntity<ErrorModel> handleInvalidUserType(InvalidUserTypeException ex) {
        ErrorModel error = new ErrorModel(HttpStatus.BAD_REQUEST, "Invalid User Type.", ex.getMessage());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(NotExistException.class)
    private ResponseEntity<ErrorModel> handleNotExist(NotExistException ex) {
        ErrorModel error = new ErrorModel(HttpStatus.NOT_FOUND, "Not Found.", ex.getMessage());

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InsufficientBalanceException.class)
    private ResponseEntity<ErrorModel> handleNotExist(InsufficientBalanceException ex) {
        ErrorModel error = new ErrorModel(HttpStatus.BAD_REQUEST, "Not Found.", ex.getMessage());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
    
}