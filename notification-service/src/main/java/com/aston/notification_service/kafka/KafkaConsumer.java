package com.aston.notification_service.kafka;

import com.aston.notification_service.mapper.EmailDTO;
import com.aston.notification_service.service.MessageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaConsumer {

    private final MessageService messageService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "notifications", groupId = "email")
    public void listen(String payload) {
        try {
            EmailDTO emailDTO = objectMapper.readValue(payload, EmailDTO.class);
            log.info("Получено сообщение из Kafka: {}", emailDTO);

            messageService.sendEmail(emailDTO);

            log.info("Email отправлен для: {}", emailDTO.to());
        } catch (Exception ex) {
            log.error("Ошибка парсинга входящего сообщения: " + ex.getMessage());
        }

    }
}
