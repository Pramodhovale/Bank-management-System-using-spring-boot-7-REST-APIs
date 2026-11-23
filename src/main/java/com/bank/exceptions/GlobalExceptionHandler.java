package com.bank.exceptions;

import com.bank.dto.ExceptionResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DuplicateCustomerException.class)
    public ResponseEntity<ExceptionResponseDto>
    handleAccountNotFoundException(DuplicateCustomerException duplicateCustomerException,
                                   WebRequest webRequest) {

        ExceptionResponseDto exceptionRequestDto = new ExceptionResponseDto(
                webRequest.getDescription(true),
                HttpStatus.BAD_REQUEST,
                duplicateCustomerException.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(exceptionRequestDto, HttpStatus.BAD_REQUEST);
    }
}
