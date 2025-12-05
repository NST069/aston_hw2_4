package com.aston.user_service.service;

import com.aston.user_service.mapper.EmailDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.http.*;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@ConditionalOnProperty(name = "notification.service", havingValue = "kafka", matchIfMissing = true)
@Primary
@RequiredArgsConstructor
public class NotificationKafkaService implements NotificationService {

    private final KafkaTemplate<String, String> kafkaTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean sendEmailToNotificationService(EmailDTO emailDTO) {
        try {
            String json = objectMapper.writeValueAsString(emailDTO);
            kafkaTemplate.send("notifications", json)
                    .whenComplete((result, ex) -> {
                        if (ex == null) {
                            log.info("Сообщение отправлено в kafka");
                        } else {
                            log.error("Ошибка отправки в kafka: " + ex.getMessage());
                        }
                    });

            return true;
        } catch (Exception e) {
            log.error("Ошибка при отправке в kafka: " + e.getMessage());
            return false;
        }
    }
}
