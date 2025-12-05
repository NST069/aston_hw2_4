package com.aston.user_service.service;

import com.aston.user_service.mapper.EmailDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationOrchestrator {

    private final NotificationService notificationService;

    public boolean sendNotification(EmailDTO emailDTO) {
        log.info("Отправка уведомления через {}",
                notificationService.getClass().getSimpleName());

        return notificationService.sendEmailToNotificationService(emailDTO);
    }

    public String getActiveServiceType() {
        return notificationService.getClass().getSimpleName();
    }
}