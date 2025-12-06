package com.aston.user_service.service;

import com.aston.user_service.mapper.EmailDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationOrchestrator {

    private final NotificationService notificationService;

    @Async
    public void sendNotification(EmailDTO emailDTO) {
        log.info("Отправка уведомления через {}",
                notificationService.getClass().getSimpleName());

        if (notificationService.sendEmailToNotificationService(emailDTO)){
            log.info("Сообщение передано на отправку");
        }
        else{
            log.error("Сообщение не было передано на отправку");
        }
    }

    public String getActiveServiceType() {
        return notificationService.getClass().getSimpleName();
    }
}