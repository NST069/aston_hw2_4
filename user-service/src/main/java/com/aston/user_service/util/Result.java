package com.aston.user_service.util;

import com.aston.user_service.controller.UserController;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.PrePersist;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.RepresentationModel;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Стандартный ответ API")
public class Result extends RepresentationModel<Result> {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Schema(description = "Флаг успешности операции", example = "true")
    private boolean isSuccessful;

    @Schema(description = "Код статуса операции", example = "200")
    private int statusCode;

    @Schema(description = "Сообщение о статусе операции", example = "Пользователь найден успешно")
    private String message;

    @Schema(description = "Ответное сообщение")
    private Object data;

    @PrePersist
    public void log() {
        if (isSuccessful) logger.info(this.message);
    }
}
