package com.aston.user_service.mapper;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;

public record UserDTO(int id,
                      @NotEmpty(message = "Поле Имя обязательно для заполнения")
                      String name,
                      @NotEmpty(message = "Поле Email обязательно для заполнения")
                      @Email(message = "Некорректный формат email адреса")
                      String email,
                      @Range(min = 1, max = 120, message = "Возраст должен быть от 1 до 120")
                      int age) {
}
