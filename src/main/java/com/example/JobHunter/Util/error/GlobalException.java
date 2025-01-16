package com.example.JobHunter.Util.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestCookieException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.example.JobHunter.domain.dto.response.RestResponse;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler(value = {
            UsernameNotFoundException.class,
            BadCredentialsException.class
    })
    public ResponseEntity<RestResponse<Object>> handlerIdexception(Exception e) {
        RestResponse<Object> response = new RestResponse<Object>();
        response.setStatusCode(HttpStatus.BAD_REQUEST.value());
        response.setError(e.getMessage());
        response.setMessage("Exception occured");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(MissingRequestCookieException.class)
    public ResponseEntity<RestResponse<Object>> MissingRequestCookieException(MissingRequestCookieException e) {
        RestResponse<Object> restResponse = new RestResponse<Object>();
        restResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
        restResponse.setError(e.getMessage());
        restResponse.setMessage("Don't have cookies or cookies is invalid!");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(restResponse);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<RestResponse<Object>> NoResourceFoundException(NoResourceFoundException e) {
        RestResponse<Object> restResponse = new RestResponse<Object>();
        restResponse.setStatusCode(HttpStatus.NOT_FOUND.value());
        restResponse.setError(e.getMessage());
        restResponse.setMessage("Resource not found");

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(restResponse);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<RestResponse<Object>> NullPointerException(NullPointerException e) {
        RestResponse<Object> restResponse = new RestResponse<Object>();
        restResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        restResponse.setError(e.getMessage());
        restResponse.setMessage("Internal server error");

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(restResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RestResponse<Object>> ValidationError(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        final List<FieldError> fieldErrors = bindingResult.getFieldErrors();

        RestResponse<Object> restResponse = new RestResponse<Object>();
        restResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
        restResponse.setError(e.getBody().getDetail());

        List<String> errors = fieldErrors.stream().map(f -> f.getDefaultMessage())
                .collect(Collectors.toUnmodifiableList());
        restResponse.setMessage(errors.size() > 1 ? errors : errors.get(0));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(restResponse);
    }

    @ExceptionHandler(value = {
            StorageException.class
    })
    public ResponseEntity<RestResponse<Object>> handlerUploadFileexception(Exception e) {
        RestResponse<Object> response = new RestResponse<Object>();
        response.setStatusCode(HttpStatus.BAD_REQUEST.value());
        response.setError(e.getMessage());
        response.setMessage("Exception occured");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

}
