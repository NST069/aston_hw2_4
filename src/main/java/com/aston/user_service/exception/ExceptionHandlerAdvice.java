package com.aston.user_service.exception;

import com.aston.user_service.util.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class ExceptionHandlerAdvice {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionHandlerAdvice.class);

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    Result handleUserNotFoundException(UserNotFoundException ex) {
        logger.error(ex.getMessage());
        return new Result(false, HttpStatus.NOT_FOUND.value(), ex.getMessage(), null);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    Result handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        logger.error(ex.getMessage());
        return new Result(false, HttpStatus.BAD_REQUEST.value(), ex.getMessage(), null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    Result handleValidationException(MethodArgumentNotValidException ex) {
        List<ObjectError> errors = ex.getBindingResult().getAllErrors();
        Map<String, String> map = new HashMap<>(errors.size());
        errors.forEach(error -> {
            String key = ((FieldError) error).getField();
            String value = error.getDefaultMessage();
            map.put(key, value);
        });

        logger.error("Представлены невалидные аргументы:\n\t"+ String.join("\n\t", map.values()));
        return new Result(false, HttpStatus.BAD_REQUEST.value(), "Представлены невалидные аргументы", map);
    }
}
