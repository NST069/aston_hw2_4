package com.aston.notification_service.controller;

import com.aston.notification_service.mapper.EmailDTO;
import com.aston.notification_service.service.MessageService;
import com.aston.notification_service.util.Result;
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
public class NotificationController implements NotificationAPI{

    private final MessageService messageService;

    @PostMapping("/sendEmail")
    public Result sendEmail(@Valid @RequestBody EmailDTO emailDTO) {
        log.debug("Сообщение принято на отправку: " + emailDTO);
        messageService.sendEmail(emailDTO);

        return new Result(true, HttpStatus.OK.value(), "Email отправлен успешно", null);
    }
}
