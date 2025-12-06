package com.aston.notification_service.controller;

import com.aston.notification_service.mapper.EmailDTO;
import com.aston.notification_service.service.MessageService;
import com.aston.notification_service.util.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
@Tag(name = "Notifications", description = "Управление уведомлениями")
public class NotificationController {

    private final MessageService messageService;

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
    @PostMapping("/sendEmail")
    public Result sendEmail(@Valid @RequestBody EmailDTO emailDTO) {
        log.debug("Сообщение принято на отправку: " + emailDTO);
        messageService.sendEmail(emailDTO);

        return new Result(true, HttpStatus.OK.value(), "Email отправлен успешно", null);
    }
}
