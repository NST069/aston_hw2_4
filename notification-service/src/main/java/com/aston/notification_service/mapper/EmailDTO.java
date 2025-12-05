package com.aston.notification_service.mapper;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

public record EmailDTO(@NotEmpty(message = "Email получателя обязателен")
                       @Email(message = "Некорректный формат email")
                       String to,
                       @NotEmpty
                       @Pattern(regexp = "create|delete",
                               message = "Недопустимое событие. Допустимые значения: create, delete")
                       String event,
                       String subject) {
}
