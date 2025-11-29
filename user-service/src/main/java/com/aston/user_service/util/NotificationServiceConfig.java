package com.aston.user_service.util;

import com.aston.user_service.service.NotificationKafkaService;
import com.aston.user_service.service.NotificationService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.core.KafkaTemplate;

@Configuration
public class NotificationServiceConfig {

    @Bean
    @ConditionalOnProperty(name = "notification.service.type", havingValue = "kafka")
    @Primary
    public NotificationService kafkaNotificationService(KafkaTemplate<String, String> kafkaTemplate) {
        return new NotificationKafkaService(kafkaTemplate);
    }
}