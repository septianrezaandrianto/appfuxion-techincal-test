package com.techincal.test.appfuxion.controller.advice;

import com.techincal.test.appfuxion.exception.CustomException;
import com.techincal.test.appfuxion.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class CustomExceptionAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(CustomException.class)
    public Map<String, Object> customExceptionHandler(CustomException ex) {
        Map<String, Object> errorMap = new HashMap<>();
        errorMap.put("responseCode", ex.getResponseCode());
        errorMap.put("responseMessage", ex.getResponseMessage());
        return errorMap;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public Map<String, Object> notFoundExceptionHandler() {
        Map<String, Object> errorMap = new HashMap<>();
        errorMap.put("responseCode", HttpStatus.NOT_FOUND.value());
        errorMap.put("responseMessage", "Data Not Found");
        return errorMap;
    }
}
