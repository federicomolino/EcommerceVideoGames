package com.videogames.videogames.Exception;

import com.videogames.videogames.Entity.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionApi{

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> erroriApi(NessunGiocoTrovato e, HttpServletRequest request){
        ErrorResponse err = new ErrorResponse(
                e.getMessage(),
                request.getRequestURI()
        );
        return new ResponseEntity<>(err, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ExceptionAddGioco.class)
    public ResponseEntity<ErrorResponse> errorApiAddGioco(ExceptionAddGioco e, HttpServletRequest request){
        ErrorResponse err = new ErrorResponse(
                e.getMessage(),
                request.getRequestURI()
        );
        return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
    }
}
