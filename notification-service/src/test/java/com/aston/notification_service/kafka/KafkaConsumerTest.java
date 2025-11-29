package com.aston.notification_service.kafka;

import com.aston.notification_service.mapper.EmailDTO;
import com.aston.notification_service.service.MessageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class KafkaConsumerTest {

    private ObjectMapper objectMapper;

    @Mock
    private MessageService messageService;

    @InjectMocks
    private KafkaConsumer kafkaConsumer;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void listen_ValidMessage_SendEmail() throws JsonProcessingException {
        EmailDTO emailDTO = new EmailDTO("test@example.com", "Subject", "Body");
        String json = this.objectMapper.writeValueAsString(emailDTO);

        this.kafkaConsumer.listen(json);

        verify(this.messageService).sendEmail(emailDTO);
    }

    @Test
    public void listen_InvalidMessage_DontSendEmail() {
        String invalidJson = "{ invalid json }";

        kafkaConsumer.listen(invalidJson);

        verify(messageService, never()).sendEmail(any());
    }

}
