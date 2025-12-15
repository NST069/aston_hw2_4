package com.aston.user_service.service;

import com.aston.user_service.mapper.EmailDTO;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@ConditionalOnProperty(name = "notification.service", havingValue = "rest", matchIfMissing = false)
@RequiredArgsConstructor
public class NotificationRestService implements NotificationService {

    private final RestTemplate restTemplate;

    @CircuitBreaker(name = "notification-service")
    @Retry(name = "notification-service")
    public boolean sendEmailToNotificationService(EmailDTO emailDTO) {
        String url = "http://notification-service:8080/api/v1/notifications/sendEmail";

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<EmailDTO> request = new HttpEntity<>(emailDTO, headers);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("Email успешно отправлен в notification service");
                return true;
            } else {
                log.error("Ошибка от notification service: " + response.getStatusCode());
                return false;
            }

        } catch (Exception e) {
            log.error("Ошибка при отправке в notification service: " + e.getMessage());
            return false;
        }
    }
}
