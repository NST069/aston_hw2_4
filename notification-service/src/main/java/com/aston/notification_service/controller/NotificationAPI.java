package com.aston.notification_service.controller;

import com.aston.notification_service.mapper.EmailDTO;
import com.aston.notification_service.util.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;

public interface NotificationAPI {

    @Operation(
            summary = "Отправить сообщение на почту",
            description = "Отправка сообщения о статусе пользователя на почту, указанную в профиле",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные пользователя",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = EmailDTO.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Сообщение отправлено",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Result.class),
                                    examples = @ExampleObject(
                                            summary = "Успешный ответ",
                                            description = "Пример успешного ответа от сервиса",
                                            value = """
                                                    {
                                                        "successful": "true",
                                                        "status": "200",
                                                        "message": "Email отправлен успешно"
                                                    }
                                                    """
                                    )
                            )
                    )
            }
    )
    Result sendEmail(@Valid @RequestBody EmailDTO emailDTO);
}
