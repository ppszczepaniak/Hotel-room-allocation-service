package com.hotelRoom.allocator.exceptions;

import com.hotelRoom.allocator.api.dto.ErrorResponse;
import com.hotelRoom.allocator.domain.exceptions.HotelContextException;
import com.hotelRoom.allocator.domain.exceptions.RoomAllocationException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationExceptions(MethodArgumentNotValidException ex) {
        StringBuilder errorMessage = new StringBuilder();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String invalidField = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errorMessage.append(String.format("field `%s`: %s", invalidField, message));
        });
        return new ErrorResponse(errorMessage.toString());
    }

    @ExceptionHandler(HttpMessageConversionException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationExceptions(HttpMessageConversionException ex) {
        return new ErrorResponse(String.format("Invalid body - please verify your input; reason: %s", ex.getMessage()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationExceptions(ConstraintViolationException ex) {
        return new ErrorResponse("invalid data; " + ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleIllegalArgumentException(IllegalArgumentException ex) {
        return new ErrorResponse("internal server error; invalid argument: " + ex.getMessage());
    }

    @ExceptionHandler(RoomAllocationException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleRoomAllocationException(RoomAllocationException ex) {
        return new ErrorResponse("internal server error; " + ex.getMessage());
    }

    @ExceptionHandler(HotelContextException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleHotelContextException(HotelContextException ex) {
        return new ErrorResponse("internal server error; " + ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleOtherExceptions() {
        return new ErrorResponse("other internal server error; please try again later");
    }
}


