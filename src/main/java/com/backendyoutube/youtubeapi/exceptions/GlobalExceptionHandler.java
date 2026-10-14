package com.backendyoutube.youtubeapi.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.Map;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

//    @ExceptionHandler(ConflictException.class)
//    public ResponseEntity<ApiErrorResponse> handleConflict(ConflictException ex, HttpServletRequest request){
//        return buildResponse(ex.getStatus(),
//                ex.getMessage(),
//                request.getRequestURI(),
//                Map.of()) ;
//    }
//    @ExceptionHandler(ResourceNotFoundException.class)
//    public ResponseEntity<ApiErrorResponse> handleNotFound(ResourceNotFoundException ex, HttpServletRequest request){
//        return buildResponse(ex.getStatus(),
//                ex.getMessage(),
//                request.getRequestURI(),
//                Map.of());
//    }
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiErrorResponse> handleApiException(ApiException ex, HttpServletRequest request){
        return buildResponse(
                ex.getStatus(),
                ex.getMessage(),
                request.getRequestURI(),
                Map.of());
    }
    private ResponseEntity<ApiErrorResponse> buildResponse(HttpStatus status, String message, String path, Map<String, String> validationErrors){
        ApiErrorResponse body =createError(status,message,path,validationErrors);
        return ResponseEntity.status(status).body(body);
    }
    private ApiErrorResponse createError(HttpStatus status, String message, String path, Map<String ,String> validationErrors){
//        HttpStatus resolveStatus=HttpStatus.resolve(status.value());
//        String errorName=resolveStatus==null ? "error": resolveStatus.getReasonPhrase();
        String errorName=status.getReasonPhrase();
        return  new ApiErrorResponse(
                LocalDateTime.now(),
                status.value(),
                errorName,
                message,
                path,
                validationErrors
                );
    }


}
