package com.aston.notification_service.util;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Стандартный ответ API")
public class Result extends RepresentationModel<Result> {

    @Schema(description = "Флаг успешности операции", example = "true")
    private boolean isSuccessful;

    @Schema(description = "Код статуса операции", example = "200")
    private int statusCode;

    @Schema(description = "Сообщение о статусе операции", example = "Пользователь найден успешно")
    private String message;

    @Schema(description = "Ответное сообщение")
    private Object data;
}
