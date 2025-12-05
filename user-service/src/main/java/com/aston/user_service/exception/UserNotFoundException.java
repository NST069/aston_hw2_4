package com.aston.user_service.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String userId) {
        super("Пользователь с Id " + userId + " не найден");
    }
}
