package com.aston.notification_service.mapper;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

@Schema(description = "Email")
public record EmailDTO(@NotEmpty(message = "Email получателя обязателен")
                       @Email(message = "Некорректный формат email")
                       @Schema(description = "Email получателя", example = "ilovespamming@gmail.com")
                       String to,
                       @NotEmpty(message = "Событие обязательно")
                       @Pattern(regexp = "create|delete",
                               message = "Недопустимое событие. Допустимые значения: create, delete")
                       @Schema(description = "Событие", example = "create", allowableValues = {"create", "delete"})
                       String event,
                       @Schema(description = "Тема письма", example = "Вы выиграли, заберите приз")
                       String subject) {
}
