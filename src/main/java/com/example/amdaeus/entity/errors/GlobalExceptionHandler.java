package com.example.amdaeus.entity.errors;

import com.example.amdaeus.dto.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(JwtExpiredException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiError handleJwtExpired(JwtExpiredException ex) {
        return new ApiError(
                HttpStatus.UNAUTHORIZED.value(),
                "JWT_EXPIRED",
                ex.getMessage()
        );
    }

    @ExceptionHandler(BussinessTripRequestNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ApiError handleBtrNotFound(BussinessTripRequestNotFoundException ex) {
        return new ApiError(
                HttpStatus.NOT_FOUND.value(),
                "BTR_NOT_FOUND",
                ex.getMessage()
        );
    }

    @ExceptionHandler(UserNotFoundExeption.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ApiError handleUserNotFound(UserNotFoundExeption ex) {
        return new ApiError(
                HttpStatus.NOT_FOUND.value(),
                "USER_NOT_FOUND",
                ex.getMessage()
        );
    }

    @ExceptionHandler(ThresholdNotFoundExeption.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ApiError handleThresholdNotFound(ThresholdNotFoundExeption ex) {
        return new ApiError(
                HttpStatus.NOT_FOUND.value(),
                "THRESHOLD_NOT_FOUND",
                ex.getMessage()
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ApiError handleIllegalArgument(IllegalArgumentException ex) {
        return new ApiError(
                HttpStatus.BAD_REQUEST.value(),
                "INVALID_ARGUMENT",
                ex.getMessage()
        );
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ApiError handleGenericException(Exception ex) {
        return new ApiError(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "INTERNAL_ERROR",
                ex.getMessage()
        );
    }
}
