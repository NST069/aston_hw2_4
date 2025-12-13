package com.aston.user_service.mapper;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;

@Schema(description = "Пользователь")
public record UserDTO(@Schema(description = "Id пользователя", example = "1")
                      int id,

                      @NotEmpty(message = "Поле Имя обязательно для заполнения")
                      @Schema(description = "Имя пользователя", example = "Райан Гослинг")
                      String name,

                      @NotEmpty(message = "Поле Email обязательно для заполнения")
                      @Email(message = "Некорректный формат email адреса")
                      @Schema(description = "Email пользователя", example = "ryangosling@bladerunners.net")
                      String email,

                      @Range(min = 1, max = 120, message = "Возраст должен быть от 1 до 120")
                      @Schema(description = "Возраст пользователя", example = "45")
                      int age) {
}
